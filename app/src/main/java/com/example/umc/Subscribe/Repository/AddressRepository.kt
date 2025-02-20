package com.example.umc.Subscribe.Repository

import android.content.Context

object AddressRepository {
    private const val PREFS_NAME = "address_prefs"
    private const val KEY_ADDRESS_ID = "address_id"

    fun saveAddressId(context: Context, addressId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_ADDRESS_ID, addressId).apply()
    }

    fun getAddressId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ADDRESS_ID, null)
    }
}
