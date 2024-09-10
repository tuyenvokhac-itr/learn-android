package com.example.composablelearning.blecore.core.command

import android.util.Log
import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.utils.DateTimeUtils

object GetDeviceInfoHandler {
    fun send(handler: DeviceHandler) {
        val cid = BioRingProto.CommandId.CID_DEV_INFO_GET

        val command = BioRingProto.CommandPacket.newBuilder().setCid(cid).build()

        val time = DateTimeUtils.getEpochTimeInMillis()
        val packet = BioRingProto.Packet.newBuilder()
            .setSid(time)
            .setType(BioRingProto.PacketType.PACKET_TYPE_COMMAND)
            .setCommand(command)
            .build()

        val data = packet.toByteArray()

        Log.d("HEHEELO", "GET DEVICE INFO")
        handler.write(data)
    }
}