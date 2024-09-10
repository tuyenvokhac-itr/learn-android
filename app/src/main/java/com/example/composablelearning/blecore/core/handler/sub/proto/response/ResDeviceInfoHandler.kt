package com.example.composablelearning.blecore.core.handler.sub.proto.response

import com.example.composablelearning.blecore.BioRingProto.Packet
import com.example.composablelearning.blecore.core.handler.DeviceHandler

object ResDeviceInfoHandler {
    fun handle(packet: Packet, handler: DeviceHandler) {

        val device = handler.getDevice()
        val info = packet.response.devInfo

        device.hwVersion = info.hardwareVersion
        device.fwVersion = info.firmwareVersion
        device.model = info.model
        device.serialNumber = info.serialNumber

        handler.callBack.onDeviceInfoUpdated(handler)
    }
}