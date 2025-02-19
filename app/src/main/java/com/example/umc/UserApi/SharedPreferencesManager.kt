package com.example.umc.UserApi

import android.content.Context

object SharedPreferencesManager {
    private const val PREF_NAME = "UserPreferences"

    // 사용자 정보를 저장할 키 정의
    private const val KEY_USER_ID = "userId"
    private const val KEY_EMAIL = "email"
    private const val KEY_NAME = "name"
    private const val KEY_BIRTH = "birth"
    private const val KEY_PROFILE_IMAGE = "profileImage"
    private const val KEY_LOGIN_METHOD = "loginMethod"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_REFRESH_TOKEN = "refreshToken"

    // 전체 사용자 데이터 저장 메서드
    fun saveUserData(context: Context, userData: Map<String, Any>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 각 필드 저장
        editor.putInt(KEY_USER_ID, userData["userId"] as Int)
        editor.putString(KEY_EMAIL, userData["email"] as String)
        editor.putString(KEY_NAME, userData["name"] as String)
        editor.putString(KEY_BIRTH, userData["birth"] as String)
        editor.putString(KEY_PROFILE_IMAGE, userData["profileImage"] as String)
        editor.putString(KEY_LOGIN_METHOD, userData["loginMethod"] as String)
        editor.putString(KEY_ACCESS_TOKEN, userData["accessToken"] as String)
        editor.putString(KEY_REFRESH_TOKEN, userData["refreshToken"] as String)

        editor.apply()
    }
    // 토큰 및 사용자 정보 저장 메서드 추가
    fun saveUserData(
        context: Context,
        userId: Int,
        accessToken: String,
        refreshToken: String? = null,
        email: String? = null
    ) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_ACCESS_TOKEN, accessToken)
            refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
            email?.let { putString(KEY_EMAIL, it) }
            apply()
        }
    }
    // 데이터 조회 메서드들
    fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun getAccessToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }

    // 로그아웃 시 데이터 삭제
    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
