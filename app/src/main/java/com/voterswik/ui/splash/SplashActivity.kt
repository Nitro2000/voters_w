package com.voterswik.ui.splash

import android.content.Intent
import android.os.Bundle
import com.voterswik.ui.dashboard.DashboardActivity
import com.voterswik.R
import com.voterswik.pref.UserPref
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.login.LoginActivity
import com.voterswik.utils.CommonUtils
import com.voterswik.utils.toast
import kotlinx.coroutines.*

class SplashActivity : BaseActivity() {
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPref = UserPref(this)

        if (CommonUtils.isInternetAvailable(this)) {
            checkLogin()
        } else {
            toast("Please check internet connection.")
        }

    }

    private fun checkLogin() {
        activityScope.launch {
            delay(3000)
            if (userPref.isLogin) {
                val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)

            }
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }

}