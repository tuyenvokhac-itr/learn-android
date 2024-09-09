package com.example.composablelearning.blecore.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import android.util.Log
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BleConnection(private val context: Context, private val callBack: BleConnectionCallBack) {
    private val TAG = "BleConnection"

    private var bluetoothGatt: BluetoothGatt? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null
    private var rxCharacteristic: BluetoothGattCharacteristic? = null
    private var dataCharacteristic: BluetoothGattCharacteristic? = null

    private var subscribeNotificationCount = 0

    private val bluetoothGattCallBack: BluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                val startDiscoverySuccess = gatt?.discoverServices()
                if (startDiscoverySuccess != true) { // ?? tai sao
                    closeGattConnection()
                    return
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                closeGattConnection()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                discoverService(gatt)
                setCharsNotification(gatt)
            } else {
                closeGattConnection()
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            Log.d(TAG, "onMtuChanged: ")
        }

        // Below SDK 33
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            when (characteristic?.uuid.toString().lowercase()) {
                BleConstant.RX_CHARACTERISTIC_UUID -> {
                    callBack.onRxDataReceived(gatt.device, characteristic.value)
                }
            }
        }

        // SDK 33
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            when (characteristic.uuid.toString().lowercase()) {
                BleConstant.RX_CHARACTERISTIC_UUID -> {
                    callBack.onRxDataReceived(gatt.device, value)
                }

                BleConstant.DATA_CHARACTERISTIC_UUID -> {
                    callBack.onDataReceived(gatt.device, value)
                }
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            super.onDescriptorWrite(gatt, descriptor, status)
            Log.d(TAG, "onDescriptorWrite ${descriptor?.characteristic?.uuid}")
            if (gatt != null) {
                setCharsNotification(gatt)
            }
        }
    }

    private fun discoverService(gatt: BluetoothGatt?) {
        if (gatt == null) return

        gatt.services.forEach{service ->
            when (service.uuid.toString()) {
                BleConstant.SERVICE_UUID -> {
                    service.characteristics.forEach { characteristic ->
                        when (characteristic.uuid.toString().lowercase()) {
                            BleConstant.TX_CHARACTERISTIC_UUID -> {
                                characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                                txCharacteristic = characteristic
                            }

                            BleConstant.RX_CHARACTERISTIC_UUID -> {
                                rxCharacteristic = characteristic
                            }

                            BleConstant.DATA_CHARACTERISTIC_UUID -> {
                                dataCharacteristic = characteristic
                            }
                        }
                    }
                }

                else -> {}
            }
        }

    }

    // Ko hieu ham nay
    private fun setCharsNotification(gatt: BluetoothGatt?) {
        if (gatt == null) return

        subscribeNotificationCount += 1
        when(subscribeNotificationCount) {
            1 -> {
                if (rxCharacteristic != null) {
                    gatt.setCharacteristicNotification(rxCharacteristic, true)
                }
            }

            2 -> {
                if (dataCharacteristic != null) {
                    gatt.setCharacteristicNotification(dataCharacteristic, true)
                    val uuid = UUID.fromString(BleConstant.CCC_BITS_UUID)
                    val descriptor = dataCharacteristic!!.getDescriptor(uuid)
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(descriptor)
                }
            }
        }

        if(subscribeNotificationCount == 2) {
            callBack.onConnected(gatt.device)
        }
    }

    private fun closeGattConnection() {
        bluetoothGatt?.let { gatt ->
            callBack.onDisconnected(gatt.device)
            gatt.disconnect()
            gatt.close()
            bluetoothGatt = null
            subscribeNotificationCount = 0
        }
    }

    fun connect(device: BluetoothDevice): Boolean {
        return try {
            bluetoothGatt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                device.connectGatt(
                    context, false, bluetoothGattCallBack, BluetoothDevice.TRANSPORT_LE
                )
            } else {
                device.connectGatt(context, false, bluetoothGattCallBack)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun disconnect() {
        closeGattConnection()
    }

    fun write(data: String) {
        writeData(data.toByteArray())
    }

    fun write(data: ByteArray) {
        writeData(data)
    }

    private fun writeData(data: ByteArray) {
        try {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                txCharacteristic?.value = data
                bluetoothGatt?.writeCharacteristic(txCharacteristic)
            } else {
                bluetoothGatt?.writeCharacteristic(txCharacteristic!!, data, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}