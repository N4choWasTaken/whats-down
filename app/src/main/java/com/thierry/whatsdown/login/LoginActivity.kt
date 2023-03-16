package com.thierry.whatsdown.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thierry.whatsdown.OverviewActivity
import com.thierry.whatsdown.R
import com.thierry.whatsdown.User
import com.thierry.whatsdown.User.Companion.login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val username = findViewById<TextView>(R.id.usernameArea).text.toString()
            val password = findViewById<TextView>(R.id.passwordArea).text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                val user = User.login(username, password)
                if (user != null) {
                    val intent = Intent(this@LoginActivity, OverviewActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                } else {
                    //TODO: Handel failed login
                }
            }
        }
    }
}