package com.example.composablelearning.blecore.core

import android.bluetooth.BluetoothDevice
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.model.AccData
import com.example.composablelearning.blecore.core.model.EcgData
import com.example.composablelearning.blecore.core.model.PpgData
import com.example.composablelearning.blecore.core.model.TempData

interface CoreHandlerCallBack {
    fun onFoundDevice(device: BluetoothDevice)

    fun onConnected(handler: DeviceHandler)

    fun onDisconnected(handler: DeviceHandler)

    fun onAccDataReceived(data: AccData, handler: DeviceHandler)

    fun onEcgDataReceived(data: EcgData, handler: DeviceHandler)

    fun onPpgDataReceived(data: PpgData, handler: DeviceHandler)

    fun onTempDataReceived(data: TempData, handler: DeviceHandler)

    fun onDeviceInfoUpdated(handler: DeviceHandler)
}