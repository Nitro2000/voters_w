package com.voterswik.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.databinding.VerifyAccountBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.login.LoginActivity
import com.voterswik.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOtpActivity : BaseActivity(), View.OnClickListener {
    private var id: String?=null
    private var userId: String?=null
    private var otp: String? = null
    private lateinit var verifyAccountBinding: VerifyAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyAccountBinding = DataBindingUtil.setContentView(this, R.layout.verify_account)

        otp= intent.getStringExtra("otp")
         userId= intent.getStringExtra("user_id")
       id= intent.getStringExtra("id")

        otp?.let { snackbar(it) }

        verifyAccountBinding.btnNext.setOnClickListener(this)
        verifyAccountBinding.backArrow.setOnClickListener(this)
        verifyAccountBinding.tvCancel.setOnClickListener(this)
        verifyAccountBinding.constraintSignup.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnNext ->{
        val enterOtp= verifyAccountBinding.edtEnterOtp.text.toString()
                if (enterOtp.isNullOrEmpty()) {
                    snackbar("Enter OTP.")
                }else if(enterOtp.equals(otp)){
                    val intent = Intent(this,ChangePasswordActivity:: class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("user_id",userId)
                    startActivity(intent)
                }else{
                    snackbar("Enter valid OTP.")
                }
            }
            R.id.backArrow ->{
                val intent = Intent(this,ForgotPasswordActivity:: class.java)
                startActivity(intent)
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