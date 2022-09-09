package com.voterswik

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VotersWikUserApplication : Application() {

    private val YOUR_CLIENT_ID: String = BuildConfig.CLIENT_ID


    override fun onCreate() {
        super.onCreate()

        // For paypal payment
        val config = CheckoutConfig(
            application = this,
            clientId = YOUR_CLIENT_ID,
            environment = Environment.SANDBOX,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        //  returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
        PayPalCheckout.setConfig(config)
    }
}