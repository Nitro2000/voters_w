package com.voterswik.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Utils @Inject constructor(@ApplicationContext val context: Context) {

    fun toaster(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun logger(message: String) {
        Log.e("Win-Millionaire-Log", message)
    }

    fun simpleAlert(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
           dialog.dismiss()
        }



        builder.show()
    }

    fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun savedDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
    }

    fun showDate(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        return sdf.format(date)
    }

    fun savedDate1(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(date)
    }

    fun showDate1(date: Date): String {
        val sdf = SimpleDateFormat("dd-MMM-yyyy")
        return sdf.format(date)
    }


    public fun fromHtml(html: String): Spanned {
        val result: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(html)
        }
        return result
    }


    /**
     * checking internet connection
     */
    @SuppressLint("MissingPermission")
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }


    fun getFormattedDate(incomingDate: String): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        var formatedDate = ""
        try {
            date = fmt.parse(incomingDate)
            val fmtOut = SimpleDateFormat("dd-MMM-yyyy")
            formatedDate = fmtOut.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatedDate
    }

 /*   fun getDeviceToken(): String? {
        var token=""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result!!.token

                // Log and toast
                // val msg = getString(R.string.msg_token_fmt, token)
                Log.d("", token)
                //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
            })
        return token
    }
*/
    @SuppressLint("HardwareIds")
    fun getDeviceId() : String{
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    companion object {

        var userType: String? = null
        var serviceID: String? = null
    }

}