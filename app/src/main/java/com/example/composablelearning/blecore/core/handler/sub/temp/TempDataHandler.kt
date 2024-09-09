package com.example.composablelearning.blecore.core.handler.sub.temp

import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.model.TempData
import com.example.composablelearning.blecore.utils.ByteUtils
import java.nio.ByteOrder

object TempDataHandler {

    fun handle(data: ByteArray, handler: DeviceHandler) {
        val tempData = parse(data)
        handler.callBack.onTempDataReceived(tempData, handler)
    }

    private fun parse(data: ByteArray): TempData {
        val data1 = ArrayList<Float>()

        for (i in 0..data.size - 4 step 4) {
            val sample1 = ByteUtils.subByteArray(data, i, 4)
            val value1 = ByteUtils.toFloat(sample1, ByteOrder.LITTLE_ENDIAN)
            data1.add(value1)
        }

        return TempData(data1)
    }
}