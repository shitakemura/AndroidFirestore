package com.example.shitakemura.androidfirestore.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.DATE_CREATED
import com.example.shitakemura.androidfirestore.Utilities.USERNAME
import com.example.shitakemura.androidfirestore.Utilities.USER_REF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        auth = FirebaseAuth.getInstance()

        createCreateButton.setOnClickListener {
            createUser()
        }

        createCancelButton.setOnClickListener {
            finish()
        }
    }

    private fun createUser() {
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()
        val username = createUsernameText.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->

                    val changeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()

                    result.user.updateProfile(changeRequest)
                            .addOnFailureListener { exception ->
                                Log.e("Exception:", "Could not update diaplay name: ${exception.localizedMessage}")
                            }

                    val data = HashMap<String, Any>()
                    data[USERNAME] = username
                    data[DATE_CREATED] = FieldValue.serverTimestamp()

                    FirebaseFirestore.getInstance().collection(USER_REF).document(result.user.uid)
                            .set(data)
                            .addOnSuccessListener {
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Exception:", "Could not add user document: ${exception.localizedMessage}")
                            }

                }
                .addOnFailureListener { exception ->
                    Log.e("Exception:", "Could not create user: ${exception.localizedMessage}")
                }

    }
}
