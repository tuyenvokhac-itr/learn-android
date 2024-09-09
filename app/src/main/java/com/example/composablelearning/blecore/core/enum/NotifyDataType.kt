package com.example.composablelearning.blecore.core.enum

enum class NotifyDataType(private var value: Int) {
    AFE(0xA0), IMU(0xA1), TEMP(0xA2);

    companion object {
        fun get(value: Int): NotifyDataType? {
            for (cmd in NotifyDataType.values()) {
                if (value == cmd.value) {
                    return cmd;
                }
            }

            return null
        }
    }
}