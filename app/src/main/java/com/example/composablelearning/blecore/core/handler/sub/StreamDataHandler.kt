package com.example.composablelearning.blecore.core.handler.sub

import android.util.Log
import com.example.composablelearning.blecore.core.enum.NotifyDataType
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.handler.sub.acc.AccDataHandler
import com.example.composablelearning.blecore.core.handler.sub.afe.AfeDataHandler
import com.example.composablelearning.blecore.core.handler.sub.temp.TempDataHandler
import com.example.composablelearning.blecore.core.model.Packet

object StreamDataHandler {
    fun handle(packet: Packet, handler: DeviceHandler) {
        Log.d("StreamDataHandler", "StreamDataHandler ${packet.type} ")
        when (packet.type) {
            NotifyDataType.AFE -> AfeDataHandler.handle(packet.data, handler)
            NotifyDataType.IMU -> AccDataHandler.handle(packet.data, handler)
            NotifyDataType.TEMP -> TempDataHandler.handle(packet.data, handler)
        }
    }
}