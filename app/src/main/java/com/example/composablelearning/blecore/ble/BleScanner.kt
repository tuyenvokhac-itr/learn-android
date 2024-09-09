package com.example.composablelearning.blecore.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings

@SuppressLint("MissingPermission")
class BleScanner(
    private val scanner: BluetoothLeScanner,
    private val onDevicesFound: (BluetoothDevice) -> Unit
) {
    private val TAG: String = "BleScanner"

    private val mScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (result.device.name != null && result.device.name.startsWith("BioRing")) {
                onDevicesFound(result.device)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }

    fun startScan() {
        stopScan()
        val settings =
            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

//        val serviceUuidBuilder = ScanFilter.Builder()
        val filters: List<ScanFilter> = arrayListOf()

        scanner.startScan(filters, settings, mScanCallback)
    }

    fun stopScan() {
        scanner.stopScan(mScanCallback)
    }
}