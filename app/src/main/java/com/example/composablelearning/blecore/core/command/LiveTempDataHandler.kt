package com.example.composablelearning.blecore.core.command

import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.utils.DateTimeUtils

object LiveTempDataHandler {
    fun send(handler: DeviceHandler, isStart: Boolean) {
        val cid =
            if (isStart) BioRingProto.CommandId.CID_STREAMING_DATA_START
            else BioRingProto.CommandId.CID_STREAMING_DATA_STOP
        val command = BioRingProto.CommandPacket.newBuilder()
            .setCid(cid)
            .setSensorType(BioRingProto.SensorType.SENSOR_TYPE_TEMP_VALUE)
            .build()

        val time = DateTimeUtils.getEpochTimeInMillis()
        val packet = BioRingProto.Packet.newBuilder()
            .setSid(time)
            .setType(BioRingProto.PacketType.PACKET_TYPE_COMMAND)
            .setCommand(command)
            .build()

        val data = packet.toByteArray()

        handler.write(data)
    }
}