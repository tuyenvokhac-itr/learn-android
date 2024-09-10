package com.example.composablelearning.blecore.core.parser

import com.example.composablelearning.blecore.core.enum.NotifyDataType
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.model.Packet
import com.example.composablelearning.blecore.utils.ByteUtils
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class DataParser(private val handler: DeviceHandler, private val callBack: (Packet) -> Unit) :
    Runnable {
    private val buffer: BlockingQueue<ByteArray> = ArrayBlockingQueue(500)

    override fun run() {
        while (!handler.executor.isShutdown && !handler.executor.isTerminated) {
            try {
                val data = buffer.take()
                parse(data)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun push(data: ByteArray) {
        buffer.put(data)
    }

    private fun parse(data: ByteArray) {
        val type = readType(data)
        if (type != null) {
            val value = readValue(data)
            callBack(Packet(type, value))
        }
    }

    private fun readType(data: ByteArray): NotifyDataType? {
        val type = data[0].toInt() and 0xFF
        return NotifyDataType.get(type)
    }

    private fun readValue(data: ByteArray): ByteArray {
        return ByteUtils.subByteArray(data, 5, data.size - 5)
    }
}