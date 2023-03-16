package com.thierry.whatsdown

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.thierry.whatsdown.chats.Chat
import com.thierry.whatsdown.database.DataBase
import com.thierry.whatsdown.login.LoginActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await

class User(_username: String, _id: String) : java.io.Serializable{
    private var id: String = _id
    var username = _username;
    var chats: List<Chat> = emptyList();

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
                            val user = User(userSnapshot.get("username").toString(), userSnapshot.id)
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

        suspend fun getChats(currentUser: User): MutableList<DocumentSnapshot> {
            val userRef = DataBase.connect().collection("users").document(currentUser.id)
            val query = DataBase.connect().collection("chats")
                .whereEqualTo("user1", userRef)
                .get()
                .await()

            val query2 = DataBase.connect().collection("chats")
                .whereEqualTo("user2", userRef)
                .get()
                .await()

            val chatDocs = mutableListOf<DocumentSnapshot>()
            chatDocs.addAll(query.documents)
            chatDocs.addAll(query2.documents)

            return chatDocs

        }

    }
}