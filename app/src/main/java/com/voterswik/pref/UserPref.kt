package com.voterswik.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.voterswik.ui.login.Data
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPref @Inject constructor(@ApplicationContext context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()
    var user: Data
        get() = gson.fromJson<Any>(preferences.getString("user", null), Data::class.java) as Data
        set(user) {
            val gson = Gson()
            val loginRes = gson.toJson(user)
            preferences.edit().putString("user", loginRes).apply()
        }
    var isLogin: Boolean
        get() = preferences.getBoolean("isLoginA", false)
        set(login) = preferences.edit().putBoolean("isLoginA", login).apply()

    fun clearPref() {
        preferences.edit().clear().apply()
    }


    fun getBio(): String? {
        return preferences.getString("bio", null)
    }

    fun setBio(bio: String?) {
        preferences.edit().putString("bio", bio).apply()
    }

    fun setName(name: String) {
        preferences.edit().putString("name", name).apply()
    }

    fun getName(): String? {
        return preferences.getString("name", null)
    }
    fun getUserId(): String? {
        return preferences.getString("user_id", null)
    }
    fun setUserId(user_id: String) {
        preferences.edit().putString("user_id", user_id).apply()
    }

    fun getToken(): String? {
        return preferences.getString("token", null)
    }
    fun setImage(image: String) {
        preferences.edit().putString("image", image).apply()
    }

    fun getImage(): String? {
        return preferences.getString("image", null)
    }

    fun setEmail(email: String?) {
        preferences.edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return preferences.getString("email", null)
    }

    fun setMobile(mobile: String?) {
        preferences.edit().putString("mobile", mobile).apply()
    }

    fun getmobile(): String? {
        return preferences.getString("mobile", null)
    }

    fun setToken(token: String?) {
        preferences.edit().putString("token", token).apply()
    }


    //for FCM Token
    fun getFcmToken(): String? {
        return preferences.getString("fcmtoken", null)
    }

    fun setFcmToken(token: String?) {
        preferences.edit().putString("fcmtoken", token).apply()
    }


}