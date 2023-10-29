package com.example.irrigo

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.irrigo.data.Feed
import com.example.irrigo.utils.ThingSpeakService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.Time
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addField = findViewById<ImageView>(R.id.add_btn)

        addField.setOnClickListener {
            val intent = Intent(this@MainActivity,FieldActivity::class.java)
            startActivity(intent)
        }

        val textView = findViewById<TextView>(R.id.textview)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thingspeak.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ThingSpeakService::class.java)


//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val response = service.getChannelData()
//                if (response != null) {
//                    val feeds = response.feeds
//                    if (feeds.isNotEmpty()) {
//                        val feed = feeds[0] // Assuming you want to display the first feed
//                        val text = "${feed.field1}"
//                        runOnUiThread {
//                            textView.text = text
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            val SMS_PERMISSION_REQUEST_CODE = 123
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), SMS_PERMISSION_REQUEST_CODE)
        } else {
            // You have permission to send SMS, so you can call sendSMS()
        }


        var currentFeedNumber = 0 // Initialize the feed number

        val timer = Timer()

        val phoneNumber = "6386309697" // Replace with the recipient's phone number
        val smsMessage = "FAULT DETECTED :: Check your device on the field.Feel free to call on 1234567890 for any help"
        var startTime: Long = 0

        timer.schedule(0, 60000) { // Schedule updates every minute (60,000 milliseconds)
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = service.getChannelData()
                    if (response != null) {
                        val feeds = response.feeds
                        if (currentFeedNumber < feeds.size) {
                            val feed = feeds[currentFeedNumber]
                            val text = "${feed.field1}"
                            runOnUiThread {
                                textView.text = text
                            }

                            if (text == "0") {
                                if (currentFeedNumber == 0) {
                                    // Record the time when '0' value is first detected
                                     startTime = System.currentTimeMillis()
                                } else {
                                    val currentTime = System.currentTimeMillis()
                                    if (currentTime - startTime >= 1 * 60 * 1000) {
                                        // Send SMS when '0' value has persisted for 5 minutes

                                        sendSMS(phoneNumber, smsMessage)
                                    }
                                }
                            } else {
                                // Reset the timer when the value is not '0'
                                currentFeedNumber = 0
                            }

                            currentFeedNumber++
                        } else {
                            // Reset the feed number when all feeds have been displayed
                            currentFeedNumber = 0
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    }

    fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}