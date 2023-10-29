package com.example.irrigo

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.irrigo.utils.AndroidUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


class LoginOtpActivity : AppCompatActivity() {

    var phoneNumber: String? = null
    var timeoutSeconds = 60L
    var verificationCode: String? = null
    var resendingToken: ForceResendingToken? = null

    private var countDownTimer: CountDownTimer? = null

    lateinit var otpInput: EditText
    lateinit var nextBtn: Button
    lateinit var progressBar: ProgressBar
    lateinit var resendOtpTextView: TextView
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)

        otpInput = findViewById<EditText>(R.id.idEdtOtp)
        nextBtn = findViewById<Button>(R.id.idBtnGetOtp)
        progressBar = findViewById<ProgressBar>(R.id.login_progress_bar)
        resendOtpTextView = findViewById<TextView>(R.id.resend_otp_textview)

        phoneNumber = intent.extras?.getString("phone")

        sendOtp(phoneNumber,false)

        nextBtn.setOnClickListener { v: View? ->
            val enteredOtp = otpInput.text.toString()
            val credential =
                PhoneAuthProvider.getCredential(verificationCode!!, enteredOtp)
            signIn(credential)
        }

        resendOtpTextView.setOnClickListener(View.OnClickListener { v: View? ->
            sendOtp(
                phoneNumber,
                true
            )
        })


    }

    fun sendOtp(phoneNumber: String?, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    AndroidUtil.showToast(applicationContext, "OTP verification failed")
                    setInProgress(false)
                }

                override fun onCodeSent(
                    s: String,
                    forceResendingToken: ForceResendingToken,
                ) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    AndroidUtil.showToast(applicationContext, "OTP sent successfully")
                    setInProgress(false)
                }
            })
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(
                builder.setForceResendingToken(resendingToken!!).build()
            )
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE)
            nextBtn.setVisibility(View.GONE)
        } else {
            progressBar.setVisibility(View.GONE)
            nextBtn.setVisibility(View.VISIBLE)
        }
    }

    fun signIn(phoneAuthCredential: PhoneAuthCredential?) {
        //login and go to next activity
        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential!!).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this@LoginOtpActivity, LoginUsernameActivity::class.java)
                intent.putExtra("phone", phoneNumber)

                startActivity(intent)
            } else {
                AndroidUtil.showToast(applicationContext, "OTP verification failed")
            }
        }
    }

//    fun startResendTimer() {
//        resendOtpTextView.isEnabled = false
//        val timer = Timer()
//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                timeoutSeconds--
//                resendOtpTextView.text = "Resend OTP in $timeoutSeconds seconds"
//                if (timeoutSeconds <= 0) {
//                    timeoutSeconds = 60L
//                    timer.cancel()
//                    runOnUiThread { resendOtpTextView.isEnabled = true }
//                }
//            }
//        }, 0, 1000)
//    }

    fun startResendTimer() {
        resendOtpTextView.isEnabled = false
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                resendOtpTextView.text = "Resend OTP in $seconds seconds"
            }

            override fun onFinish() {
                resendOtpTextView.isEnabled = true
            }
        }.start()
    }
}