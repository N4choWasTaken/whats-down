package com.thierry.whatsdown.chats

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.thierry.whatsdown.User
import com.thierry.whatsdown.chats.messages.Messages
import com.thierry.whatsdown.database.DataBase
import kotlinx.coroutines.tasks.await

class Chat(_user1: User, _user2: User) {
    var user1: User = _user1
    var user2: User = _user2


    companion object {
        suspend fun getMessagesFromChat(chatId: String): List<Messages> {
            val db = DataBase.connect()
            val chatRef = db.collection("chats").document(chatId)

            val dbMessages = db.collection("messages")
                .whereEqualTo("chat", chatRef)
                .get()
                .await()

            val messages = dbMessages.documents.map { doc ->
                val user = doc.get("user").toString() ?: ""
                val content = doc.get("content").toString() ?: ""
                Messages(content, chatRef, user)
            }

            return messages
        }
    }
}
