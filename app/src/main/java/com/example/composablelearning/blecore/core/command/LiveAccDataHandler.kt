package com.example.composablelearning.blecore.core.command

import com.example.composablelearning.blecore.BioRingProto
import com.example.composablelearning.blecore.BioRingProto.CommandPacket
import com.example.composablelearning.blecore.BioRingProto.Packet
import com.example.composablelearning.blecore.BioRingProto.SensorType
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.utils.DateTimeUtils

object LiveAccDataHandler {
    fun send(handler: DeviceHandler, isStart: Boolean) {
        val cid =
            if (isStart) BioRingProto.CommandId.CID_STREAMING_DATA_START
            else BioRingProto.CommandId.CID_STREAMING_DATA_STOP

        val command = CommandPacket.newBuilder()
            .setCid(cid)
            .setSensorType(SensorType.SENSOR_TYPE_IMU_VALUE)
            .build()

        val time = DateTimeUtils.getEpochTimeInMillis()
        val packet = Packet.newBuilder()
            .setSid(time)
            .setType(BioRingProto.PacketType.PACKET_TYPE_COMMAND)
            .setCommand(command)
            .build()

        val data = packet.toByteArray()

        handler.write(data)
    }
}