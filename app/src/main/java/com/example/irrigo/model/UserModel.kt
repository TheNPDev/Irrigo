package com.example.irrigo.model


import com.google.firebase.Timestamp

data class UserModel(
    var phone: String? = null,
    var username: String? = null,
    var selectedGender: String? = null,
    var createdTimestamp: Timestamp? = null
)

