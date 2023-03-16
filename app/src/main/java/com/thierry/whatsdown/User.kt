package com.thierry.whatsdown

import android.content.ContentValues.TAG
import android.util.Log
import com.thierry.whatsdown.database.DataBase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class User(_username: String) {
    var username = _username;

    companion object {
        suspend fun login(username: String, password: String): User? =
            suspendCancellableCoroutine { continuation ->
                val query = DataBase.connect().collection("users")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                query.get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val userSnapshot = querySnapshot.documents[0]
                            val user = User(userSnapshot.get("username").toString())
                            Log.d(TAG, "Found user: $user")
                            continuation.resume(user, onCancellation = null)
                        } else {
                            Log.d(TAG, "No user found")
                            continuation.resume(null, onCancellation = null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting user", exception)
                        continuation.resume(null, onCancellation = null)
                    }
            }
    }
}