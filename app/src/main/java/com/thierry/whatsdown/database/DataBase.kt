package com.thierry.whatsdown.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DataBase {
    companion object {
        fun connect(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }
    }

}