package com.example.umc.UserApi

import android.content.Context

object SharedPreferencesManager {

    private const val PREF_NAME = "UserPreferences"
    private const val KEY_USER_ID = "userId"

    // SharedPreferences에 userId 저장하는 메서드
    fun saveUserId(context: Context, userId: Int) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_USER_ID, userId)  // userId 저장
        editor.apply()  // 비동기적으로 저장
    }

    // SharedPreferences에서 userId 불러오는 메서드
    fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_USER_ID, -1)  // 기본값 -1
    }

    // SharedPreferences에 값 저장 여부 체크
    fun hasUserId(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.contains(KEY_USER_ID)  // userId가 저장되어 있는지 여부 반환
    }
}
