package com.example.composablelearning.blecore.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BleStateController private constructor(context: Context) {
    private var mBtAdapter: BluetoothAdapter? = null
    private var mListener: BluetoothStateListener? = null


    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action?.equals(BluetoothAdapter.ACTION_STATE_CHANGED) == true) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

                if (mListener == null) return

                when (state) {
                    BluetoothAdapter.STATE_OFF -> mListener!!.onBluetoothOff()
                    BluetoothAdapter.STATE_ON -> mListener!!.onBluetoothOn()
                }

            }
        }
    }

    init {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBtAdapter = bluetoothManager.adapter
        val btIntent = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(mReceiver, btIntent)
    }

    companion object {
        @Volatile
        private var mBleController: BleStateController? = null

        fun getInstance(context: Context): BleStateController =
            mBleController ?: synchronized(this) {
                mBleController ?: BleStateController(context).also { mBleController = it }
            }
    }

    fun isBluetoothEnabled(): Boolean {
        return mBtAdapter?.isEnabled ?: false
    }

    fun setListener(listener: BluetoothStateListener?) {
        mListener = listener
    }
}

interface BluetoothStateListener {
    fun onBluetoothOn()

    fun onBluetoothOff()
}