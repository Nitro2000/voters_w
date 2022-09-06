package com.voterswik.utils

interface ApiConstant {
    companion object {

        //Local server url
        const val BASE_URL = "http://182.76.237.238/~apitest/voters_wik/api/"
//        const val BASE_URL = "http://172.16.0.238/~apitest/voters_wik/api/"
        const val LOGIN_URL = "login"
        const val CREATE_USER = "createuser"

        val IS_NOTIFICATION = "is_notification"
        var cartcount: Int? = 0
        var isItem: Boolean? = false
        var cartAmount: String? = ""
        var cartCurrency: String? = ""
        const val MY_PREFERENCES = "prefs"

        var DEVICE_ID = "device_id"
        var ISLOGIN = "is_login"
        var NOT_FIRST_TIME = "not_first_time"
        var IS_GUEST = "is_guest"
        var MOBILE_NO = "mobile_no"
        var DEVICE = "A"
        var USER_ID = "user_id"
        var NAME = "name"
        var EMAIL = "email"
        var LAT = "lat"
        var LONG = "long"
        var PROFILE_PIC = "profile_pic"
        var tabIndex: Int = 0

    }
}