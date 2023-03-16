package com.thierry.whatsdown

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.thierry.whatsdown.chats.Chat
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
        }
    }

}