package com.voterswik.utils

import android.content.Context
import javax.inject.Singleton

@Singleton
class PrefManager(
   val _context: Context?,
) {

    // shared pref mode
    var PRIVATE_MODE = 0

    // Shared preferences file name
    private val PREF_NAME = "intro-slider"

    var pref = _context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor = pref!!.edit()




    private val IS_FIRST_TIME_LAUNCH = "intro-slider"



    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor!!.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor!!.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref!!.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }

}