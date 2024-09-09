package com.example.composablelearning.blecore.core.model

import com.example.composablelearning.blecore.core.enum.NotifyDataType

data class Packet(val type: NotifyDataType, val data: ByteArray)
