package com.example.composablelearning.blecore.core.handler.sub.proto.notification

import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.handler.sub.proto.notification.device_info.NotiDeviceInfoHandler

object ProtoNotificationHandler {
    fun handle(packet: BioRingProto.Packet, handler: DeviceHandler) {
        when (packet.notification.nid) {
            BioRingProto.NotificationId.NID_BATTERY_LEVEL_CHANGED -> {
                NotiDeviceInfoHandler.handle(packet, handler)
            }

            BioRingProto.NotificationId.NID_CHARGING_STATUS_CHANGED -> {
                NotiDeviceInfoHandler.handle(packet, handler)
            }

            else -> {}
        }
    }
}