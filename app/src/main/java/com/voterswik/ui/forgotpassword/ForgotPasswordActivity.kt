package com.voterswik.ui.forgotpassword

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.databinding.ActivityForgotPasswordBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.login.LoginActivity
import com.voterswik.ui.login.LoginViewModel
import com.voterswik.ui.signup.SignUpActivity
import com.voterswik.utils.AppConstant
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {
    private val mLoginViewModel: LoginViewModel by viewModels()
    private lateinit var activityForgotPasswordBinding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

     activityForgotPasswordBinding.btnNext.setOnClickListener(this)
        activityForgotPasswordBinding.backArrow.setOnClickListener(this)
        activityForgotPasswordBinding.tvCancel.setOnClickListener(this)
        activityForgotPasswordBinding.constraintSignup.setOnClickListener(this)
        mLoginViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        mLoginViewModel.forgotResponse.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this, "OTP Sent Successfully...", Toast.LENGTH_LONG).show()
                userPref.user = it.data!!
//                userPref.isLogin = true
                val intent = Intent(this, VerifyOtpActivity :: class.java)
                intent.putExtra("id", it.data!!.id.toString())
                intent.putExtra("otp", it.data!!.otp)
                intent.putExtra("user_id", it.data!!.userId)
                startActivity(intent)
                finishAffinity()
            } else {
                toast(it.message)
            }
        }
        specificTextBold()
    }

    private fun specificTextBold() {
        val text = AppConstant.FORGOT_PASSWORD
        val spannableString= SpannableString(text)
        val boldSpan= StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan,7,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        activityForgotPasswordBinding.tvForgotPassword.text = spannableString
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnNext ->{
                if(activityForgotPasswordBinding.edtEmailIdOrMobileNumber.text.toString().isNullOrEmpty()){
                    toast("Please enter email and mobile number.")
                }
                else {
                    mLoginViewModel.forgotPassword(
                        activityForgotPasswordBinding.edtEmailIdOrMobileNumber.text.toString(),

                        )
                }
            }
            R.id.backArrow ->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvCancel ->{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.constraintSignup ->{
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }


}