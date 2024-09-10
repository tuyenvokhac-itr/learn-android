package com.example.composablelearning.blecore.core

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.composablelearning.blecore.ble.BleScanner
import com.example.composablelearning.blecore.ble.BleStateController
import com.example.composablelearning.blecore.ble.BluetoothStateListener
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.handler.DeviceHandlerCallBack
import com.example.composablelearning.blecore.core.model.AccData
import com.example.composablelearning.blecore.core.model.EcgData
import com.example.composablelearning.blecore.core.model.PpgData
import com.example.composablelearning.blecore.core.model.TempData
import com.example.composablelearning.blecore.utils.SingletonHolder

class CoreHandler private constructor(private val context: Context): DeviceHandlerCallBack, BluetoothStateListener{
    companion object : SingletonHolder<CoreHandler, Context>(::CoreHandler)
    private var deviceHandler: DeviceHandler? = null
    private var callBack: CoreHandlerCallBack? = null

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return@lazy bluetoothManager.adapter
    }

    private var scanDeviceList = ArrayList<BluetoothDevice>()
    private val bleScanner: BleScanner = BleScanner(bluetoothAdapter.bluetoothLeScanner) {
        if (!isExistInScanList(it)) {
            scanDeviceList.add(it)
            callBack?.onFoundDevice(it)
        }
    }

    private var bleStateController = BleStateController.getInstance(context)

    init {
        bleStateController.setListener(this)
    }

    private fun isExistInScanList( device: BluetoothDevice): Boolean {
        for (device in scanDeviceList) {
            if (device.address == device.address) {
                return true
            }
        }

        return false
    }

    fun setListener(callBack: CoreHandlerCallBack) {
        this.callBack = callBack
    }

    fun startScan() {
        scanDeviceList.clear()
        bleScanner.startScan()
    }

    fun stopScan() {
        bleScanner.stopScan()
    }

    fun connect(address: String) {
        for (device in scanDeviceList) {
            if (device.address == address) {
                if(deviceHandler == null) {
                    deviceHandler = DeviceHandler(context, device, this)
                    deviceHandler?.connect()
                } else {
                    deviceHandler?.connect()
                }
            }
        }
    }

    fun disconnect(address: String) {
        deviceHandler?.disconnect()
        deviceHandler = null
    }

    fun getDevice(): DeviceHandler? {
        return deviceHandler
    }

    //********************* BLUETOOTH STATE CHANGED ************************************************

    override fun onBluetoothOn() {

    }

    override fun onBluetoothOff() {
        deviceHandler?.let {
            disconnect(it.getDevice().getAddress())
        }
    }

    //********************* DEVICE CONNECTION CHANGED ************************************************

    override fun onConnected(handler: DeviceHandler) {
        scanDeviceList.clear()
        callBack?.onConnected(handler)
    }

    override fun onDisConnected(handler: DeviceHandler) {
        callBack?.onDisconnected(handler)
    }

    //********************* LIVE DATA CHANGED ************************************************

    override fun onAccDataReceived(data: AccData, handler: DeviceHandler) {
        callBack?.onAccDataReceived(data, handler)
    }

    override fun onEcgDataReceived(data: EcgData, handler: DeviceHandler) {
        callBack?.onEcgDataReceived(data, handler)
    }

    override fun onPpgDataReceived(data: PpgData, handler: DeviceHandler) {
        callBack?.onPpgDataReceived(data, handler)
    }

    override fun onTempDataReceived(data: TempData, handler: DeviceHandler) {
        callBack?.onTempDataReceived(data, handler)
    }

    //********************* DEVICE INFO CHANGED ****************************************************

    override fun onDeviceInfoUpdated(handler: DeviceHandler) {
        callBack?.onDeviceInfoUpdated(handler)
    }
}