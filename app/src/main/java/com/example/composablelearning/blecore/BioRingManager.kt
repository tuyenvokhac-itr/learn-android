package com.example.composablelearning.blecore

import android.content.Context
import android.util.Log
import com.example.composablelearning.blecore.ble.BleStateController
import com.example.composablelearning.blecore.ble.BluetoothStateListener

/** This class is used for testing
 *
 */
class BioRingManager(private val context: Context) {
    private val TAG: String = "BioRingManager"
    fun isBleEnabled() : Boolean {
        return try {
            BleStateController.getInstance(context).isBluetoothEnabled()
        } catch (e: Exception) {
            Log.d(TAG, "isBleEnabled: exception: ${e.message}")
            false
        }
    }

    fun listenBleState(listener: BluetoothStateListener) {
        BleStateController.getInstance(context).setListener(listener)
    }
}