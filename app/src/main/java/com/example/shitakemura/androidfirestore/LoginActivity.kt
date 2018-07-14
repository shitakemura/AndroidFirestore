package com.example.shitakemura.androidfirestore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginClicked(view: View) {

    }

    fun loginCreateClicked(view: View) {
        val createIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createIntent)
    }
}
