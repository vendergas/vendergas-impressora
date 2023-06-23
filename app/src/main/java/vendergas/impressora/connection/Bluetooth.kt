package vendergas.impressora.connection

import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class Bluetooth(var ctx: Context) {

    private var adapter: BluetoothAdapter? = null
    private var socket: BluetoothSocket? = null
    private var device: BluetoothDevice? = null
    private var istream: InputStream? = null
    var ostream: BufferedOutputStream? = null

    companion object {
        private val BLUETOOTH_SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }

    private fun setupBluetoothReceiver() {
        val btReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                handleBtEvent(context, intent)
            }
        }
        val eventFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        eventFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        ctx.registerReceiver(btReceiver, eventFilter)
    }

    private fun handleBtEvent(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("Bluetooth", "action received: " + action!!)

        if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            Log.i("Bluetooth", "found device: " + device.name)
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
            Log.d("Bluetooth", "discovery complete")
        }
    }

    val isConnected: Boolean
        get() = socket != null && socket!!.isConnected

    val isEnabled: Boolean
        get() = adapter != null && adapter!!.isEnabled

    fun Enable(): Boolean {
        Log.d("Bluetooth", "starting bluetooth")
        setupBluetoothReceiver()
        adapter = BluetoothAdapter.getDefaultAdapter()

        if (adapter != null) {

            if (adapter!!.isEnabled) return true

            adapter!!.enable()

            try {
                Thread.sleep(5000, 0)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            adapter = BluetoothAdapter.getDefaultAdapter()

            return adapter!!.isEnabled
        }
        return false
    }

    fun Disable(): Boolean {
        if (adapter != null) {
            if (adapter!!.isEnabled) adapter!!.disable()
            return !adapter!!.isEnabled
        }
        return false
    }

    fun GetBondedDevices(): Set<BluetoothDevice> {
        return adapter!!.bondedDevices
    }

    fun Open(mac: String): Boolean {
        try {
            this.Close()
            device = adapter!!.getRemoteDevice(mac)
            adapter!!.cancelDiscovery()
            socket = device!!.createRfcommSocketToServiceRecord(BLUETOOTH_SPP_UUID)
            socket!!.connect()
            istream = socket!!.inputStream
            ostream = BufferedOutputStream(socket!!.outputStream)
            return true
        } catch (e: IOException) {
            return false
        }

    }

    fun Close(): Boolean {
        try {
            if (ostream != null) ostream!!.flush()
            if (socket != null) socket!!.close()

            socket = null
        } catch (e: IOException) {
            return false
        }

        return true
    }

    fun Write(buffer: ByteArray, count: Int): Boolean {
        try {
            ostream!!.write(buffer, 0, count)
        } catch (e: IOException) {
            return false
        }

        return true
    }

    fun Read(buffer: ByteArray, length: Int): Boolean {
        try {
            istream!!.read(buffer, 0, length)
        } catch (e: IOException) {
            return false
        }

        return true
    }
}
