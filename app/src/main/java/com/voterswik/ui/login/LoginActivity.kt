package com.voterswik.ui.login

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
import com.hbb20.CountryCodePicker
import com.voterswik.ui.dashboard.DashboardActivity
import com.voterswik.R
import com.voterswik.databinding.ActivityLoginBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.forgotpassword.ForgotPasswordActivity
import com.voterswik.ui.signup.SignUpActivity
import com.voterswik.utils.AppConstant
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener , CountryCodePicker.OnCountryChangeListener{

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private val mLoginViewModel: LoginViewModel by viewModels()
    private var selected_country_code:String?=null
    private var loginFrom:String="email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        specificTextBold()
        activityLoginBinding.constraintSignup.setOnClickListener(this)
        activityLoginBinding.tvForgotPassword.setOnClickListener(this)
        activityLoginBinding.btnLogin.setOnClickListener(this)


        mLoginViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        mLoginViewModel.error.observe(this){
            it
        }

     activityLoginBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
         if (R.id.btnEmail == checkedId) {
             loginFrom="email"
             activityLoginBinding.constraintEmail.visibility=View.VISIBLE
             activityLoginBinding.constraintPhone.visibility=View.GONE

         }else{
             loginFrom="mobile"
             activityLoginBinding.constraintPhone.visibility=View.VISIBLE
             activityLoginBinding.constraintEmail.visibility=View.GONE

         }


     }

        mLoginViewModel.loginResponse.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this, "Logged in Successfully...", Toast.LENGTH_LONG).show()
                userPref.user = it.data!!
                userPref.isLogin = true
                userPref.setToken(it.data!!.token)
                userPref.setName(it.data?.name!!)
                userPref.setEmail(it.data!!.email)
                userPref.setMobile(it.data!!.mobile.toString())
                userPref.setUserId(it.data!!.userId!!.toString())
                userPref.setBio(it.data!!.bio)
                it.data!!.image?.let { it1 -> userPref.setImage(it1)}
//                userPref.setUserId(it!!.data!!.Id.toString())
                   val intent = Intent(this, DashboardActivity :: class.java)
                   startActivity(intent)
                   finishAffinity()
            } else {
                toast(it.message)
            }
        }

    }

    private fun specificTextBold() {
        val text = AppConstant.HELLO_BEAUTIFUL
        val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 6, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        activityLoginBinding.tvHelloBeautiful.text = spannableString
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                var userName=""
                var password=""


                if ( loginFrom=="email"&&  activityLoginBinding.edtEmailId.text.toString().isNullOrEmpty()
                ) {
                    toast("Please enter your valid email address")
                }


                else if (loginFrom=="email"&& activityLoginBinding.edtPassword.text.toString().isNullOrEmpty()
                ) {
                    toast(" password field at least must have entered one capital letter, one special character, and varchar values and password length should be minimum of 6 letters")
                }

                else if ( loginFrom=="mobile"&&  activityLoginBinding.edtMobileNumber.text.toString().isNullOrEmpty()
                ) {
                    toast("Please enter your valid mobile number")
                }

                else if ( loginFrom=="mobile"&&  activityLoginBinding.edtPhonePassword.text.toString().isNullOrEmpty()
                ) {
                    toast(" password field at least must have entered one capital letter, one special character, and varchar values and password length should be minimum of 6 letters")
                }


                else {

                    if(loginFrom=="email")
                    {
                        userName=activityLoginBinding.edtEmailId.text.toString()
                        password=activityLoginBinding.edtPassword.text.toString()
                    }
                    else
                    {
                        userName=activityLoginBinding.edtMobileNumber.text.toString()
                        password=activityLoginBinding.edtPhonePassword.text.toString()
                    }



                    mLoginViewModel.userLogin(
                        "1",
                        userName,
                        password,
                        "Android",
                        "Android"
                    )
                }
            }
            R.id.constraintSignup -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)

            }
            R.id.tvForgotPassword -> {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)

            }

        }
    }
    override fun onCountrySelected() {
        selected_country_code = activityLoginBinding.spCountry.selectedCountryCodeWithPlus
    }

}