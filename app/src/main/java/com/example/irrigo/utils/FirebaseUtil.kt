package com.example.irrigo.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseUtil {


    companion object {
        fun currentUserId(): String? {
            return FirebaseAuth.getInstance().uid
        }

        fun isLoggedIn(): Boolean {
            return currentUserId() != null
        }
        fun getUsersReference(currentUserId: String?): DocumentReference? {
            return FirebaseFirestore.getInstance().collection("users").document(currentUserId!!)
        }

        fun currentUserDetails(): DocumentReference? {
            return FirebaseFirestore.getInstance().collection("users").document(currentUserId()!!)
        }
        fun getUserModelReference(currentUserId: String?): DocumentReference? {
            return FirebaseFirestore.getInstance().collection("details").document(currentUserId!!)
        }
    }
}