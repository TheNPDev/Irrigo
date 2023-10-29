package com.example.irrigo

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.irrigo.model.UserModel
import com.example.irrigo.utils.AndroidUtil
import com.example.irrigo.utils.FirebaseUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot


class LoginUsernameActivity : AppCompatActivity() {

    lateinit var usernameInput: EditText
    lateinit var letMeInBtn: Button
    lateinit var progressBar: ProgressBar
//    lateinit var phoneNumber: String
    var userModel: UserModel?= null
    lateinit var genderChipGroup:ChipGroup
    lateinit var selectedGender: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_username)

        usernameInput = findViewById(R.id.idEdtUsername);
        letMeInBtn = findViewById(R.id.idBtnLetmein);
        progressBar =findViewById(R.id.login_progress_bar);
        genderChipGroup = findViewById<ChipGroup>(R.id.gender_chip_group)

        genderChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedChip = group.findViewById<Chip>(checkedId)
            selectedGender = selectedChip?.text.toString()
        }



        getUsername()

        letMeInBtn.setOnClickListener { v: View? -> setUsername() }

    }

    fun setUsername() {
        val username = usernameInput.text.toString()
        val phoneNumber = intent.extras?.getString("phone").toString()

        if (username.isEmpty() || username.length < 3) {
            usernameInput.error = "Username length should be at least 3 chars"
            return
        }
        setInProgress(true)
        if (userModel != null) {
            userModel!!.username = username
        } else {
            userModel =
                UserModel(phoneNumber, username,selectedGender, Timestamp.now())
        }
        FirebaseUtil.currentUserDetails()!!.set(userModel!!).addOnCompleteListener { task ->
            setInProgress(false)
            if (task.isSuccessful) {
                val intent = Intent(this@LoginUsernameActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    fun getUsername() {
        setInProgress(true)
        FirebaseUtil.currentUserDetails()?.get()
            ?.addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    val doc = task.result
                   Log.d(TAG, doc.toString())
                    if (doc != null && doc.exists()) {
                        userModel = doc.toObject(UserModel::class.java)

                        if (userModel != null) {
                            usernameInput.setText(userModel!!.username)
                            val selected = userModel?.selectedGender
                            for (i in 0 until genderChipGroup.childCount) {
                                val chip = genderChipGroup.getChildAt(i) as Chip
                                if (chip.text == selected) {
                                    chip.isChecked = true
                                    break // Exit the loop once the chip is selected
                                }
                            }
                        }
                    }
                }
            })
    }


    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.visibility = View.VISIBLE
            letMeInBtn.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            letMeInBtn.visibility = View.VISIBLE
        }
    }
}