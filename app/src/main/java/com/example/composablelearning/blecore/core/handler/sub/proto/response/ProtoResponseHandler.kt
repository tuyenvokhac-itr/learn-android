package com.example.composablelearning.blecore.core.handler.sub.proto.response

import android.util.Log
import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.BioRingProto.Packet
import com.example.composablelearning.blecore.core.handler.DeviceHandler

object ProtoResponseHandler {
    fun handle(packet: Packet, handler: DeviceHandler) {
        val command = packet.response.cid
        Log.d("ResponseDataHandler", "ResponseDataHandler $command")

        when (command) {
            BioRingProto.CommandId.CID_DEV_INFO_GET -> {
                ResDeviceInfoHandler.handle(packet, handler)
            }

            else -> {}
        }
    }
}