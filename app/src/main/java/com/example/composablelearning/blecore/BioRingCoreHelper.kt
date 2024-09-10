package com.example.composablelearning.blecore

import android.content.Context
import android.util.Log
import com.example.composablelearning.blecore.ble.BleStateController
import com.example.composablelearning.blecore.core.CoreHandler
import com.example.composablelearning.blecore.core.command.LiveAccDataHandler
import com.example.composablelearning.blecore.core.command.LiveEcgDataHandler
import com.example.composablelearning.blecore.core.command.LivePpgDataHandler
import com.example.composablelearning.blecore.core.command.LiveTempDataHandler
import com.example.composablelearning.blecore.core.handler.DeviceHandler
import com.example.composablelearning.blecore.utils.SingletonHolder

class BioRingCoreHelper private constructor(private val context: Context) {
    companion object : SingletonHolder<BioRingCoreHelper, Context>(::BioRingCoreHelper)

    // ******************* Send command *************************

    fun getDevice(): Map<String, Any> {
        try {
            val handler = CoreHandler.getInstance(context).getDevice()

            if (handler != null) {
                return createDeviceMap(handler)
            }

            return HashMap()
        } catch (e: Exception) {
            e.printStackTrace()
            return HashMap()
        }
    }

    fun isEnabled(): Boolean {
        return try {
            BleStateController.getInstance(context).isBluetoothEnabled();
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun startScan() : Boolean {
        return try {
            CoreHandler.getInstance(context).startScan()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun stopScan() : Boolean {
        return try {
            CoreHandler.getInstance(context).stopScan()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun connect(address: String?) : Boolean {
        try {
            if (address == null) {
                return false
            }

            CoreHandler.getInstance(context).connect(address)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun disconnect(address: String) : Boolean {
        return try {
            CoreHandler.getInstance(context).disconnect(address)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun liveAccData(address: String, isStart: Boolean): Boolean {
        return try {
            val deviceHandler = CoreHandler.getInstance(context).getDevice()
            deviceHandler?.let {
                LiveAccDataHandler.send(deviceHandler, isStart)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun liveEcgData(address: String, isStart: Boolean): Boolean {
        return try {
            val deviceHandler = CoreHandler.getInstance(context).getDevice()
            deviceHandler?.let {
                LiveEcgDataHandler.send(deviceHandler, isStart)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun livePbgData(address: String, isStart: Boolean): Boolean {
        return try {
            val deviceHandler = CoreHandler.getInstance(context).getDevice()
            deviceHandler?.let {
                LivePpgDataHandler.send(deviceHandler, isStart)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun liveTempData(address: String, isStart: Boolean): Boolean {
        return try {
            val deviceHandler = CoreHandler.getInstance(context).getDevice()
            deviceHandler?.let {
                LiveTempDataHandler.send(deviceHandler, isStart)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun createDeviceMap(handler: DeviceHandler): Map<String, Any> {
        return mapOf(
            "name" to handler.getDevice().getName(),
            "address" to handler.getDevice().getAddress(),
            "isCharging" to handler.getDevice().isCharging,
            "batteryLevel" to handler.getDevice().batteryLevel,
            "hwVersion" to handler.getDevice().hwVersion,
            "fwVersion" to handler.getDevice().fwVersion,
            "model" to handler.getDevice().model,
            "serialNumber" to handler.getDevice().serialNumber
        )
    }

    // ******************* Listen event *************************


}