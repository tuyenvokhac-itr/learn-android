package com.example.composablelearning.blecore.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context

class BlePeripheral(
    private val context: Context,
    private val device: BluetoothDevice,
    private val callBack: BleConnectionCallBack
) {
    private val connection = BleConnection(context, callBack)
    var batteryLevel: Int = 0
    var isCharging: Boolean = false

    var hwVersion: String = ""
    var fwVersion: String = ""
    var model: String = ""
    var serialNumber: String = ""

    fun connect() {
        connection.connect(device)
    }

    fun disconnect() {
        connection.disconnect()
    }

    fun write(data: ByteArray) {
        connection.write(data)
    }

    fun write(data: String) {
        connection.write(data)
    }

    fun getAddress(): String {
        return device.address
    }

    @SuppressLint("MissingPermission")
    fun getName(): String {
        return device.name
    }
}