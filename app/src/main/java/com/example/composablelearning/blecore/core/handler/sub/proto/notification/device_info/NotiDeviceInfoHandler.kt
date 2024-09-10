package com.example.composablelearning.blecore.core.handler.sub.proto.notification.device_info

import android.util.Log
import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.core.handler.DeviceHandler


object NotiDeviceInfoHandler {
    fun handle(packet: BioRingProto.Packet, handler: DeviceHandler) {

        val device = handler.getDevice()
        when (packet.notification.nid) {
            BioRingProto.NotificationId.NID_BATTERY_LEVEL_CHANGED -> {
                device.batteryLevel = packet.notification.batteryLevel
                Log.d("NotiDeviceInfoHandler", "NID_BATTERY_LEVEL_CHANGED ${device.batteryLevel}")
            }

            BioRingProto.NotificationId.NID_CHARGING_STATUS_CHANGED -> {
                device.isCharging = packet.notification.charging
                Log.d("NotiDeviceInfoHandler", "NID_CHARGING_STATUS_CHANGED ${device.isCharging}")
            }

            else -> {}
        }

        handler.callBack.onDeviceInfoUpdated(handler)
    }
}