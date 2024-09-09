package com.example.composablelearning.blecore.core.handler

import com.example.composablelearning.blecore.core.model.AccData
import com.example.composablelearning.blecore.core.model.EcgData
import com.example.composablelearning.blecore.core.model.PpgData
import com.example.composablelearning.blecore.core.model.TempData

interface DeviceHandlerCallBack {
    fun onConnected(handler: DeviceHandler)

    fun onDisConnected(handler: DeviceHandler)

    // LIVE Data
    fun onAccDataReceived(data: AccData, handler: DeviceHandler)

    fun onEcgDataReceived(data: EcgData, handler: DeviceHandler)

    fun onPpgDataReceived(data: PpgData, handler: DeviceHandler)

    fun onTempDataReceived(data: TempData, handler: DeviceHandler)

    // DEVICE INFO
    fun onDeviceInfoUpdated(handler: DeviceHandler)
}