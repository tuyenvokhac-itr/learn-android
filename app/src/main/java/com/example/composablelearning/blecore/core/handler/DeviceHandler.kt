package com.example.composablelearning.blecore.core.handler

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.example.composablelearning.blecore.ble.BleConnectionCallBack
import com.example.composablelearning.blecore.ble.BlePeripheral
import com.example.composablelearning.blecore.core.command.GetDeviceInfoHandler
import com.example.composablelearning.blecore.core.handler.sub.ProtoDataHandler
import com.example.composablelearning.blecore.core.handler.sub.StreamDataHandler
import com.example.composablelearning.blecore.core.parser.DataParser
import com.example.composablelearning.blecore.utils.ThreadPoolCreater
import java.util.Timer
import kotlin.concurrent.timerTask

class DeviceHandler(
    context: Context,
    device: BluetoothDevice,
    val callBack: DeviceHandlerCallBack
) : BleConnectionCallBack {
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
        // Get device info
        Timer().schedule(timerTask { GetDeviceInfoHandler.send(this@DeviceHandler) }, 2000)
    }

    override fun onDisconnected(btDevice: BluetoothDevice) {
        callBack.onDisConnected(this)
    }

    override fun onDataReceived(btDevice: BluetoothDevice, byteArray: ByteArray) {
        parser.push(byteArray)
    }

    override fun onRxDataReceived(btDevice: BluetoothDevice, byteArray: ByteArray) {
        ProtoDataHandler.handle(byteArray, this)
    }

    fun connect() {
        peripheral.connect()
    }

    fun disconnect() {
        peripheral.disconnect()
    }

    fun isEqual(address: String): Boolean {
        return peripheral.getAddress() == address
    }

    fun getDevice(): BlePeripheral {
        return peripheral
    }

    fun write(data: ByteArray) {
        peripheral.write(data)
    }
}