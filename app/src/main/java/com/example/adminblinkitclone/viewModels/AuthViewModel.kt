package com.example.adminblinkitclone.viewModels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.adminblinkitclone.models.Admin
import com.example.adminblinkitclone.utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {


    private val _verificationId = MutableStateFlow<String?>(null)
    private val _otpSent = MutableStateFlow(false)
    val otpSent = _otpSent

    private val _isSignedInSuccessfully = MutableStateFlow(false)
    val isSignedInSuccessfully = _isSignedInSuccessfully
    private val _isACurrentUser = MutableStateFlow(false)
    val isACurrentUser = _isACurrentUser

    init {
        utils.getAuthInstance().currentUser?.let {
            _isACurrentUser.value = true
        }
    }

    fun sendOTP(userNumber : String,activity : Activity){

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){


            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {}

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                _verificationId.value = verificationId
                _otpSent.value = true

            }


        }

        val options = PhoneAuthOptions.newBuilder(utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber")
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
        utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val uid = utils.getCurrentUserId()
                    if (uid.isNotEmpty()) {
                        val user = Admin(uid = uid, userPhoneNumber = userNumber)
                        FirebaseDatabase.getInstance().getReference("Admins")
                            .child("AdminInfo").child(uid).setValue(user)
                        _isSignedInSuccessfully.value = true
                    } else {
                        // This is rare, but can happen
                        utils.showToast(utils.appContext!!, "Something went wrong. Please try again.")
                    }
                } else {
                    utils.showToast(utils.appContext!!, "OTP verification failed.")
                }
            }



    }

}