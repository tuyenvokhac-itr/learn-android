package com.example.composablelearning.blecore.ble

object BleConstant {
    const val SERVICE_UUID = "6e400000-b5a3-f393-e0a9-e50e24dcca9e"
    const val MAX_ACCEPTED_RSSI = -70
    const val REQUEST_MTU_SIZE = 512

    // Receive response from device
    const val RX_CHARACTERISTIC_UUID = "6e400002-b5a3-f393-e0a9-e50e24dcca9e"

    // Transfer command to device
    const val TX_CHARACTERISTIC_UUID = "6e400001-b5a3-f393-e0a9-e50e24dcca9e"

    // Receive live data from device
    const val DATA_CHARACTERISTIC_UUID = "6e400003-b5a3-f393-e0a9-e50e24dcca9e"

    const val CCC_BITS_UUID = "00002902-0000-1000-8000-00805f9b34fb"

}