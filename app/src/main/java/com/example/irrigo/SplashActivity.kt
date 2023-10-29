package com.example.irrigo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.irrigo.utils.FirebaseUtil


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            if (FirebaseUtil.isLoggedIn()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginPhoneNumberActivity::class.java))
            }
            startActivity(Intent(this@SplashActivity, LoginPhoneNumberActivity::class.java))
            finish()
        }, 1500)
    }
}