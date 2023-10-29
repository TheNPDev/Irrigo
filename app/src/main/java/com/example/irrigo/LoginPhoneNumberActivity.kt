package com.example.irrigo


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity


class LoginPhoneNumberActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone_number)
        val phoneInput = findViewById<EditText>(R.id.idEdtPhoneNumber)
        val sendOtpBtn = findViewById<Button>(R.id.idBtnGetOtp)
        val progressBar = findViewById<ProgressBar>(R.id.login_progress_bar)
        progressBar.setVisibility(View.GONE)
        sendOtpBtn.setOnClickListener {
            val phoneNumber = phoneInput.text.toString().trim()

            if (isValidPhoneNumber(phoneNumber)) {
                val fullPhoneNumber = "+91$phoneNumber"

                val intent = Intent(this, LoginOtpActivity::class.java)
                intent.putExtra("phone", fullPhoneNumber)
                startActivity(intent)
            } else {
                phoneInput.error = "Phone number not valid"
            }
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 10
    }
}