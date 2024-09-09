package com.example.composablelearning.blecore.core.handler

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.example.composablelearning.blecore.ble.BleConnectionCallBack
import com.example.composablelearning.blecore.ble.BlePeripheral
import com.example.composablelearning.blecore.core.handler.sub.StreamDataHandler
import com.example.composablelearning.blecore.core.parser.DataParser
import com.example.composablelearning.blecore.utils.ThreadPoolCreater

class DeviceHandler(context: Context, device: BluetoothDevice, val callBack: DeviceHandlerCallBack) : BleConnectionCallBack {
    private val peripheral: BlePeripheral = BlePeripheral(context, device, this)

    private var parser: DataParser = DataParser(this) {
        StreamDataHandler.handle(it, this)
    }

    val executor = ThreadPoolCreater.create()

    init {
        executor.submit(parser)
    }

    override fun onConnected(btDevice: BluetoothDevice) {
        callBack.onConnected(this)
        TODO("Not yet implemented")
    }

    override fun onDisconnected(btDevice: BluetoothDevice) {
        callBack.onDisConnected(this)
    }

    override fun onDataReceived(btDevice: BluetoothDevice, byteArray: ByteArray) {
        parser.push(byteArray)
    }

    override fun onRxDataReceived(btDevice: BluetoothDevice, byteArray: ByteArray) {
        TODO("Not yet implemented")
    }

    fun connect() {
        peripheral.connect()
    }

    fun disconnect() {
        peripheral.disconnect()
    }

    fun isEqual(address: String) : Boolean {
        return  peripheral.getAddress() == address
    }

    fun getDevice(): BlePeripheral {
        return peripheral
    }

    fun write(data: ByteArray) {
        peripheral.write(data)
    }
}