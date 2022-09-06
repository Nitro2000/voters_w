package com.voterswik.ui.forgotpassword

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.voterswik.R
import com.voterswik.databinding.SetNewPasswordBinding
import com.voterswik.ui.BaseActivity
import com.voterswik.ui.login.LoginActivity
import com.voterswik.ui.login.LoginViewModel
import com.voterswik.ui.signup.SignUpActivity
import com.voterswik.utils.AppConstant
import com.voterswik.utils.CommonUtils
import com.voterswik.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity(), View.OnClickListener {
    private var userId: String? = null
    private var id: String? = null
    private val mLoginViewModel: LoginViewModel by viewModels()
    private lateinit var setNewPasswordBinding: SetNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNewPasswordBinding = DataBindingUtil.setContentView(this, R.layout.set_new_password)

        id = intent.getStringExtra("id")
        userId = intent.getStringExtra("user_id")
        specificTextBold()
        setNewPasswordBinding.btnDone.setOnClickListener(this)
        setNewPasswordBinding.backArrow.setOnClickListener(this)
        setNewPasswordBinding.constraintSignup.setOnClickListener(this)
        setNewPasswordBinding.tvCancel.setOnClickListener(this)

        mLoginViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        mLoginViewModel.changePasswordResponse.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this, "Set password Successfully...", Toast.LENGTH_LONG).show()
                userPref.user = it.data!!
//                userPref.isLogin = true
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                toast(it.message)
            }
        }
    }


    private fun specificTextBold() {
        val text = AppConstant.CREATE_PASSWORD
        val spannableString = SpannableString(text)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, 7, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        setNewPasswordBinding.tvCreatePassword.text = spannableString
    }
    private fun checkValidation(): Boolean {
        when {
            !CommonUtils.isValidPassword(setNewPasswordBinding.edtPassword.text.toString()) -> {
                setNewPasswordBinding.edtPassword.error =
                    "Password should contain at least 1 digit,1 lowercase , 1 uppercase and 1 special symbol length should between 6-20"
                Toast.makeText(applicationContext, getString(R.string.password), Toast.LENGTH_LONG)
                    .show()
                return false
            }
            setNewPasswordBinding.edtConfirmPassword.text.toString().isEmpty() -> {
                setNewPasswordBinding.edtConfirmPassword.error = "Please enter confirm Password"
                setNewPasswordBinding.edtConfirmPassword.requestFocus()
                return false
            }
            setNewPasswordBinding.edtConfirmPassword.text.toString() != setNewPasswordBinding.edtPassword.text.toString() -> {
                setNewPasswordBinding.edtConfirmPassword.error = "Confirm Password do not match"
                setNewPasswordBinding.edtConfirmPassword.requestFocus()
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.backArrow -> {
                val intent = Intent(this, VerifyOtpActivity::class.java)
                startActivity(intent)
            }
            R.id.btnDone -> {
                if(checkValidation()){
                    mLoginViewModel.changePassword(
                        setNewPasswordBinding.edtPassword.text.toString(),
                        id!!
                    )
                }
               /* if (CommonUtils.isValidPassword(setNewPasswordBinding.edtPassword.text == null || TextUtils.isEmpty(
                        setNewPasswordBinding.edtPassword.text.toString().trim()
                    ))
                ) {
                    snackbar("Please enter password")
                } else if (!CommonUtils.isPasswordConfirmPasswordSame(
                        setNewPasswordBinding.edtPassword.text.toString().trim(),
                        setNewPasswordBinding.edtConfirmPassword.text.toString().trim()
                    )
                ) {
                    snackbar("Password mismatch")
                } else {
                    mLoginViewModel.changePassword(
                        setNewPasswordBinding.edtPassword.text.toString(),
                        id!!
                    )
                }*/
            }
            R.id.constraintSignup -> {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.tvCancel -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }


        }
    }
}