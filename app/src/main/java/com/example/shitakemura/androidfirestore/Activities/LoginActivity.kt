package com.example.shitakemura.androidfirestore.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.shitakemura.androidfirestore.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            login()
        }

        loginCreateButton.setOnClickListener {
            val createIntent = Intent(this, CreateUserActivity::class.java)
            startActivity(createIntent)
        }
    }

    private fun login() {
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {exception ->
                    Log.e("Exception", "Could not sign in user - ${exception.localizedMessage}")
                }
    }
}
