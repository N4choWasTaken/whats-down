package com.thierry.whatsdown

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.thierry.whatsdown.database.DataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        GlobalScope.launch(Dispatchers.Main) {
            var currentUser = intent.getSerializableExtra("user")
            var chats = User.getChats(currentUser as User)
            Log.d(TAG, "onCreate: $chats")
            for (i in 0 until chats.size) {
                val button = Button(this@OverviewActivity)
                var users = User.getUsersFromChat(chats[i].id)

                if (users[1] == (currentUser).username) {
                    button.text = users[0]
                } else {
                    button.text = users[1]
                }

                button.setOnClickListener {
                    val intent = Intent(this@OverviewActivity, DmActivity::class.java)
                    intent.putExtra("user", currentUser)
                    intent.putExtra("chatId", chats[i].id)
                    val currentUserRef = DataBase.connect().collection("users").document(currentUser.id)
                    intent.putExtra("userRef", currentUserRef.id)
                    startActivity(intent)
                    finish()
                }
                val layout = findViewById<LinearLayout>(R.id.chats)
                layout.addView(button)

            }
        }
    }
}