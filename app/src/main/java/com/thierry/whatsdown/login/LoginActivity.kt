package com.thierry.whatsdown.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thierry.whatsdown.R
import com.thierry.whatsdown.database.DataBase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DataBase.connect()
    }
}