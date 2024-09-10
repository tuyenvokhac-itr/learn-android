package com.example.composablelearning.blecore.core.handler.sub

import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.handler.sub.proto.notification.ProtoNotificationHandler
import com.example.composablelearning.blecore.core.handler.sub.proto.response.ProtoResponseHandler


object ProtoDataHandler {
    fun handle(data: ByteArray, handler: DeviceHandler) {

        val packet = BioRingProto.Packet.parseFrom(data)

        if(packet.hasNotification()) {
            ProtoNotificationHandler.handle(packet, handler)
        }

        if (packet.hasResponse()) {
            ProtoResponseHandler.handle(packet, handler)
        }
    }
}