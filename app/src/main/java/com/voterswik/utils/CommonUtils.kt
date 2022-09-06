package com.voterswik.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.voterswik.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object CommonUtils {

    fun setFragment(fragment: Fragment, removeStack: Boolean, activity: FragmentActivity, mContainer: Int) {
        val fragmentManager = activity.supportFragmentManager
        val ftTransaction = fragmentManager.beginTransaction()
        if (removeStack) {


             fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            ftTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            ftTransaction.replace(mContainer, fragment)
            //ftTransaction.addToBackStack(null)
        } else {
            ftTransaction.replace(mContainer, fragment)
            //ftTransaction.addToBackStack(null)
        }
        ftTransaction.commit()
    }

    fun addFragment(fragment: Fragment,activity: FragmentActivity, mContainer: Int){
        val fragmentManager = activity.supportFragmentManager
        val ftTransaction = fragmentManager.beginTransaction()
        ftTransaction.add(mContainer,fragment)
        ftTransaction.commit()
    }

    fun getNumberOfFragmentInContainer(activity: FragmentActivity) : Int{
        val fragmentManager = activity.supportFragmentManager
        return fragmentManager.backStackEntryCount
    }

   /* fun getCurrentFragment(activity: FragmentActivity) : Fragment?{
        val fragmentManager = activity.supportFragmentManager
        return fragmentManager.findFragmentById(R.id.frame_Container)
    }*/
    /*To convert string date from one format to another format*/
    fun getDate(currentFormat: String, requiredFormat: String, dateString: String): String? {
        var result = ""
        if (dateString.isNullOrEmpty()) {
            return result
        }
        val formatterOld =
            SimpleDateFormat(currentFormat, Locale.getDefault())
        val formatterNew =
            SimpleDateFormat(requiredFormat, Locale.getDefault())
        var date: Date? = null
        try {
            date = formatterOld.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date != null) {
            result = formatterNew.format(date)
        }
        return result
    }

    fun isValidMail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN: String =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{6,}"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()

    }
    fun isPasswordConfirmPasswordSame(password : String,confirmPassword : String): Boolean {
        return password == confirmPassword
    }
    /**
     * @return boolean
     * @description This method is used to check internet connection.
     */
    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    val network = cm.activeNetwork
                    return when {
                        network != null -> {
                            val networkCapabilities = cm.getNetworkCapabilities(network)
                            when {
                                networkCapabilities != null -> {
                                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                }
                                else -> {
                                    return false
                                }
                            }
                        }
                        else -> false
                    }
                }
                else -> {
                    val netInfo = cm.activeNetworkInfo
                    return netInfo != null && netInfo.isConnectedOrConnecting
                }
            }
        } catch (e: Exception) {
            return false
        }
    }
}