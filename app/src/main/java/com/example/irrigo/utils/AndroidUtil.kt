package com.example.irrigo.utils

import android.content.Context
import android.widget.Toast




class AndroidUtil {


    companion object {
        fun showToast(applicationContext: Context?, s: String) {
            Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
        }
    }
}