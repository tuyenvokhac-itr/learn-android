package com.example.composablelearning.blecore.core.handler.sub.afe

import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.core.model.EcgData
import com.example.composablelearning.blecore.core.model.PpgData
import com.example.composablelearning.blecore.utils.ByteUtils
import java.nio.ByteOrder

object AfeDataHandler {
    private const val ppgRed = 0
    private const val ppgIr = 1
    private const val ecg = 11

    fun handle(data: ByteArray, handler: DeviceHandler) {
        val ecgData = ArrayList<Int>()
        val ppgRedData = ArrayList<Int>()
        val ppgIrData = ArrayList<Int>()

        for (i in 0..data.size - 4 step 4) {
            val sample =
                ByteUtils.toInteger(ByteUtils.subByteArray(data, i, 4), ByteOrder.LITTLE_ENDIAN)

            when ((sample and 0x00f00000) shr 20) {
                ppgRed -> {
                    val value = sample and 0x000fffff
                    ppgRedData.add(value)
                }

                ppgIr -> {
                    val value = sample and 0x000fffff
                    ppgIrData.add(value)
                }

                ecg -> {
                    val value = sample shl 14
                    ecgData.add(value)
                }

                else -> {}
            }
        }

        if (ecgData.isNotEmpty()) {
            handler.callBack.onEcgDataReceived(EcgData(ecgData), handler)
        }

        if (ppgRedData.isNotEmpty() || ppgIrData.isNotEmpty()) {
            handler.callBack.onPpgDataReceived(PpgData(ppgRedData, ppgIrData), handler)
        }
    }
}