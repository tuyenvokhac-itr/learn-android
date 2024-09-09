package com.example.composablelearning.blecore.core.handler.sub.acc

import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.model.AccData
import com.example.composablelearning.blecore.utils.ByteUtils
import java.nio.ByteOrder

object AccDataHandler {
    fun handle(data: ByteArray, handler: DeviceHandler) {
        val accData = parse(data)
        handler.callBack.onAccDataReceived(accData, handler)
    }

    private fun parse(data: ByteArray): AccData {
        val data1 = ArrayList<Short>()
        val data2 = ArrayList<Short>()
        val data3 = ArrayList<Short>()

        for (i in 0..data.size - 6 step 6) {
            val sample1 = ByteUtils.subByteArray(data, i, 2)
            val sample2 = ByteUtils.subByteArray(data, i + 2, 2)
            val sample3 = ByteUtils.subByteArray(data, i + 4, 2)
            val value1 = ByteUtils.toShort(sample1, ByteOrder.LITTLE_ENDIAN)
            val value2 = ByteUtils.toShort(sample2, ByteOrder.LITTLE_ENDIAN)
            val value3 = ByteUtils.toShort(sample3, ByteOrder.LITTLE_ENDIAN)

            data1.add(value1)
            data2.add(value2)
            data3.add(value3)
        }

        return AccData(data1, data2, data3)
    }
}