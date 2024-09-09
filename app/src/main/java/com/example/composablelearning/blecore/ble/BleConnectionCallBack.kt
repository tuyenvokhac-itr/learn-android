package com.example.composablelearning.blecore.ble

import android.bluetooth.BluetoothDevice

interface BleConnectionCallBack {

    fun onConnected(btDevice: BluetoothDevice)

    fun onDisconnected(btDevice: BluetoothDevice)

    fun onDataReceived(btDevice: BluetoothDevice, byteArray: ByteArray)

    fun onRxDataReceived(btDevice: BluetoothDevice, byteArray: ByteArray)
}