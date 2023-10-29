package com.example.irrigo


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.irrigo.model.FieldModel
import com.example.irrigo.model.UserModel
import com.example.irrigo.utils.FirebaseUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore





class FieldActivity : AppCompatActivity() {

    var fieldModel: FieldModel?= null
    var userModel: UserModel? = null
    lateinit var nameEdittext : EditText
    lateinit var state : String
    lateinit var district : String
    lateinit var tehsil : String
    lateinit var village : String
    lateinit var crop : String
    lateinit var accountEdittext : EditText
    lateinit var khasraEdittext : EditText
    var currentUserId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)
        currentUserId = FirebaseUtil.currentUserId()



        val stateArray = arrayOf("Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
            "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
            "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
            "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
            "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal")

        val districtUP = arrayOf("Agra", "Prayagraj (Allahabad)", "Aligarh", "Amethi", "Amroha", "Ayodhya (Faizabad)", "Azamgarh", "Baghpat", "Bahraich", "Ballia",
            "Balrampur", "Banda", "Barabanki", "Bareilly", "Basti", "Bijnor", "Budaun", "Bulandshahr", "Chandauli", "Chitrakoot", "Deoria",
            "Etah", "Etawah", "Firozabad", "Gautam Buddh Nagar (Noida)", "Ghaziabad", "Ghazipur", "Gonda", "Gorakhpur", "Hamirpur", "Hapur",
            "Hardoi", "Hathras", "Jalaun", "Jaunpur", "Jhansi", "Kannauj", "Kanpur", "Kasganj", "Kaushambi", "Kushinagar", "Lakhimpur Kheri",
            "Lalitpur", "Lucknow", "Mahoba", "Mainpuri", "Mathura", "Mau", "Meerut", "Mirzapur", "Moradabad", "Muzaffarnagar", "Pilibhit",
            "Pratapgarh", "Raebareli", "Rampur", "Saharanpur", "Sambhal", "Sant Kabir Nagar", "Shahjahanpur", "Shamli", "Shravasti", "Siddharthnagar",
            "Sitapur", "Sonbhadra", "Sultanpur", "Unnao", "Varanasi")

        val tehsilBarabanki = arrayOf("Nawabganj","Barabanki", "Fatehpur", "Haidergarh", "Ramsanehi Ghat", "Tehseel")
        val villageNawabganj = arrayOf("Lakshberbajha","Rajapur","Jyori","Bara Gaon","Dadra","Suryapur","Khapraila","Rasauli")
        val cropArray = arrayOf("Rice","Wheat","Ragi","Dragon Fruits","Potatoes","Corriander","Mustard")







        val states = findViewById<Spinner>(R.id.State_spinner)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            stateArray
        )
        states.setAdapter(adapter)
        states.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long,
            ) {
                val selected_state = states.getSelectedItem()
                state = selected_state.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        val districts = findViewById<Spinner>(R.id.districtSpinner)
        val adapter1: ArrayAdapter<String> = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            districtUP
        )
        districts.setAdapter(adapter1)
        districts.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long,
            ) {
                val selected_District = districts.getSelectedItem()
                district = selected_District.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        val tehsils = findViewById<Spinner>(R.id.tehsilSpinner)
        val adapter2 : ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            tehsilBarabanki
        )
        tehsils.setAdapter(adapter2)
        tehsils.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long,
            ) {
                val selected_tehsil = tehsils.getSelectedItem()
                tehsil = selected_tehsil.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        val villages = findViewById<Spinner>(R.id.villageSpinner)
        val adapter3 : ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            villageNawabganj
        )
        villages.setAdapter(adapter3)
        villages.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long,
            ) {
                val selected_village = villages.getSelectedItem()
                village = selected_village.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        val crops = findViewById<Spinner>(R.id.cropSpinner)
        val adapter4 : ArrayAdapter<String> = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            cropArray
        )
        crops.setAdapter(adapter4)
        crops.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long,
            ) {
                val selected_crop = crops.getSelectedItem()
                crop = selected_crop.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        })

        val btn = findViewById<Button>(R.id.verify)
        btn.setOnClickListener {
            addFieldDetails()
        }




    }

    fun addFieldDetails(){
        nameEdittext = findViewById<EditText>(R.id.name)
        val name = nameEdittext.text.toString()
        accountEdittext = findViewById<EditText>(R.id.accountSpinner)
        val account = accountEdittext.text.toString()
        khasraEdittext = findViewById<EditText>(R.id.khasraSpinner)
        val rfid = khasraEdittext.text.toString()
        FirebaseUtil.getUserModelReference(currentUserId)
        fieldModel  =  FieldModel(name,state,district,tehsil,village,account,rfid,crop)
        FirebaseUtil.getUserModelReference(currentUserId)
            ?.set(fieldModel!!)
            ?.addOnSuccessListener { void ->
                // Document successfully set
                Toast.makeText(applicationContext, "Data saved successfully!", Toast.LENGTH_SHORT).show()
            }
            ?.addOnFailureListener { e ->
                // Handle the error here
                Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_SHORT).show()
            }




    }
}