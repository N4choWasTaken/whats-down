package com.thierry.whatsdown

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.firestore.DocumentReference
import com.thierry.whatsdown.chats.Chat
import com.thierry.whatsdown.chats.Chat.Companion.writeMessage
import com.thierry.whatsdown.database.DataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dm)


        GlobalScope.launch(Dispatchers.Main) {

            var messagesFromChat =
                Chat.getMessagesFromChat(intent.getSerializableExtra("chatId").toString())
            val layout = findViewById<LinearLayout>(R.id.messages)


            for (i in messagesFromChat.indices) {
                val textView = TextView(this@DmActivity)
                textView.text = messagesFromChat[i].content
                layout.addView(textView)
            }

            findViewById<Button>(R.id.sendMessageButton).setOnClickListener {
                val chatId = intent.getSerializableExtra("chatId").toString()
                val chatRef = DataBase.connect().collection("chats").document(chatId)
                val currentUserRefId = intent.getStringExtra("userRef")
                val currentUserRef = DataBase.connect().collection("users").document(
                    currentUserRefId!!
                )


                GlobalScope.launch(Dispatchers.Main) {
                    writeMessage(
                        findViewById<TextView>(R.id.messagetextView).text.toString(),
                        currentUserRef,
                        chatRef
                    )
                    val textView = TextView(this@DmActivity)
                    textView.text = findViewById<TextView>(R.id.messagetextView).text.toString()
                    layout.addView(textView)
                    findViewById<TextView>(R.id.messagetextView).text = ""
                }
            }
        }
    }
}