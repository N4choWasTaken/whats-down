package com.thierry.whatsdown

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        GlobalScope.launch(Dispatchers.Main) {
            var chats = User.getChats(intent.getSerializableExtra("user") as User)
            Log.d(TAG, "onCreate: $chats")
            for (i in 0 until chats.size) {
                val button = Button(this@OverviewActivity)
                button.text = "Click Me"

                button.setOnClickListener {

                }
                val layout = findViewById<LinearLayout>(R.id.chats)
                layout.addView(button)
            }
        }
    }
}