package vendergas.impressora.base

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreference(context: Context) {

    private val sharedPreferences: SharedPreferences

    init { sharedPreferences = context.getSharedPreferences("VENDERGAS_IMPRESSORA", Context.MODE_APPEND)  }

    enum class KEYS(val key: String) {
        DELIVERYMAN("entregador"),
        CURREMT_ORDER("order"),
        LOGGED("logged"),
        AVAILABLE("disponivel"),
        REFUSED_ORDERS("refusedOrders"),
        EMAIL("email"),
        FOREGROUND("foreground"),
        CONFIRMED_NOTIFICATIONS("confirmedNotifications")
    }

    fun <T> getObject(key: KEYS, classOfT: Class<T>, defValue: String? = null): T = Gson().fromJson(sharedPreferences.getString(key.toString(), defValue), classOfT)
    fun <T> setObject(key: KEYS, value: T) = sharedPreferences.edit().putString(key.toString(), Gson().toJson(value)).apply()

    fun getString(key: KEYS, defValue: String? = null): String? = sharedPreferences.getString(key.toString(), defValue)
    fun setString(key: KEYS, value: String) = sharedPreferences.edit().putString(key.toString(), value).apply()

    fun getBoolean(key: KEYS, defValue: Boolean? = false): Boolean? = sharedPreferences.getBoolean(key.toString(), defValue!!)
    fun setBoolean(key: KEYS, value: Boolean) = sharedPreferences.edit().putBoolean(key.toString(), value).apply()

    fun getInt(key: KEYS, defValue: Int? = 0): Int? = sharedPreferences.getInt(key.toString(), defValue!!)
    fun setInt(key: KEYS, value: Int) = sharedPreferences.edit().putInt(key.toString(), value).apply()

    fun remove(key: KEYS) = sharedPreferences.edit().remove(key.toString()).apply()

    fun clear() = sharedPreferences.edit().clear().apply()

}