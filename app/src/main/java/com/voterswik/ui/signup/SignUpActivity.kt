package com.voterswik.ui.signup

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
import com.voterswik.R
import com.voterswik.databinding.ActivitySignupBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.login.LoginActivity
import com.voterswik.utils.AppConstant
import com.voterswik.utils.CommonUtils
import com.voterswik.utils.CommonUtils.isValidPassword
import com.voterswik.utils.Util
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity(), View.OnClickListener  , CountryCodePicker.OnCountryChangeListener{

    private val signupViewModel: SignUpViewModel by viewModels()
    private lateinit var activitySignupBinding: ActivitySignupBinding


    private var selected_country_code:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        specificTextBold()
        activitySignupBinding.constraintLogin.setOnClickListener(this)



        activitySignupBinding.btnSignup.setOnClickListener(this)
        signupViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        signupViewModel.signUpResponse.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this, "Your Account is Registered Successfully", Toast.LENGTH_LONG)
                    .show()
                userPref.user = it.data!!
                userPref.isLogin = true
                val intent = Intent(this, LoginActivity ::class.java)
                startActivity(intent)
              
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
        activitySignupBinding.tvHelloBeautiful.text = spannableString
    }

    private fun checkValidation(): Boolean {
        when {
            activitySignupBinding.edtName.text.toString().isEmpty() -> {
                activitySignupBinding.edtName.error = "Please enter Name"
                activitySignupBinding.edtName.requestFocus()
                return false
            }
            activitySignupBinding.edtEmailId.text.toString().isEmpty() -> {
                activitySignupBinding.edtEmailId.error = "Please enter E-mail ID"
                activitySignupBinding.edtEmailId.requestFocus()
                return false
            }
            !CommonUtils.isValidMail(activitySignupBinding.edtEmailId.text.toString()) -> {
                activitySignupBinding.edtEmailId.error = "Please enter valid E-mail ID"
                activitySignupBinding.edtEmailId.requestFocus()
                return false
            }
            activitySignupBinding.edtMobileNumber.text.toString().isEmpty() -> {
                activitySignupBinding.edtMobileNumber.error = "Please enter Mobile Number"
                activitySignupBinding.edtMobileNumber.requestFocus()
                return false
            }
            activitySignupBinding.edtMobileNumber.text.toString().length != 10 -> {
                activitySignupBinding.edtMobileNumber.error = "Please enter valid Mobile Number"
                activitySignupBinding.edtMobileNumber.requestFocus()
                return false
            }
            activitySignupBinding.edtPassword.text.toString().isEmpty() -> {
                activitySignupBinding.edtPassword.error = "Please enter Password"
                activitySignupBinding.edtPassword.requestFocus()
                return false
            }

            !isValidPassword(activitySignupBinding.edtPassword.text.toString()) -> {
                activitySignupBinding.edtPassword.error =
                    "Password should contain at least 1 digit,1 lowercase , 1 uppercase and 1 special symbol length should between 6-20"
                Toast.makeText(applicationContext, getString(R.string.password), Toast.LENGTH_LONG)
                    .show()
                return false
            }
            activitySignupBinding.edtConfirmPassword.text.toString().isEmpty() -> {
                activitySignupBinding.edtConfirmPassword.error = "Please enter confirm Password"
                activitySignupBinding.edtConfirmPassword.requestFocus()
                return false
            }
            activitySignupBinding.edtConfirmPassword.text.toString() != activitySignupBinding.edtPassword.text.toString() -> {
                activitySignupBinding.edtConfirmPassword.error = "Confirm Password do not match"
                activitySignupBinding.edtConfirmPassword.requestFocus()
                Toast.makeText(
                    applicationContext,
                    "Confirm Password do not match",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            else -> return true
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSignup -> {

                if(checkValidation()){
                    signupViewModel.userSignupApi(
                        activitySignupBinding.edtName.text.toString(),
                        activitySignupBinding.edtEmailId.text.toString(),
                        activitySignupBinding.edtMobileNumber.text.toString(),
                        activitySignupBinding.edtPassword.text.toString(),
                        "Android",
                        "Android"
                    )

                }
            }

            R.id.constraintLogin
                -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCountrySelected() {
        selected_country_code = activitySignupBinding.spCountry.selectedCountryCodeWithPlus
    }



}




/*  if (activitySignupBinding.edtName.text.toString().trim() == "") {
                   toast("Please enter your full name.")

               } else if (activitySignupBinding.edtEmailId.text.toString()
                       .trim() == "" || !activitySignupBinding.edtEmailId.text.toString().trim()
                       .matches(emailPattern.toRegex())
               ) {
                   toast("Please enter your valid email address.")

               } else if (activitySignupBinding.edtMobileNumber.text.toString()
                       .trim() == "" || activitySignupBinding.edtMobileNumber.text.toString()
                       .trim().length < 10
               ) {
                   toast("Please enter your valid mobile phone number")
               } else if (activitySignupBinding.edtPassword.text.toString()
                       .trim() == "" || activitySignupBinding.edtPassword.text.toString()
                       .trim().length < 6 || activitySignupBinding.edtPassword.text.toString()
                       .trim().matches(PASSWORD_PATTERN.toRegex())
               ) {
                   toast("Please enter your password of minimum 6 digits")
               } else if (activitySignupBinding.edtConfirmPassword.text.toString()
                       .trim() == "" || activitySignupBinding.edtConfirmPassword.text.toString()
                       .trim().length < 6 || activitySignupBinding.edtConfirmPassword.text.toString()
                       .trim().matches(PASSWORD_PATTERN.toRegex())
               ) {
                   toast("Please enter your confirm password of minimum 6 digits")
               } else if (isPasswordConfirmPasswordSame(
                       activitySignupBinding.edtPassword.text.toString()
                           .trim(), activitySignupBinding.edtConfirmPassword.text.toString()
                           .trim()
                   )
               ) {
                   toast("password and confirm password are mismatch")
               } else {
                   signupViewModel.userSignupApi(
                       activitySignupBinding.edtName.text.toString(),
                       activitySignupBinding.edtEmailId.text.toString(),
                       activitySignupBinding.edtMobileNumber.text.toString(),
                       activitySignupBinding.edtPassword.text.toString(),
                       "Android",
                       "Android"
                   )
               }*/