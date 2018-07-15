package com.example.shitakemura.androidfirestore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {

        }

        loginCreateButton.setOnClickListener {
            val createIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createIntent)
        }
    }
}
