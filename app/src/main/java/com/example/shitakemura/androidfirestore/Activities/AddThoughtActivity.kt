package com.example.shitakemura.androidfirestore.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_tought.*

class AddThoughtActivity : AppCompatActivity() {
    private var selectedCategory = FUNNY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tought)

        addFunnyButton.setOnClickListener {
            setCategory(FUNNY)
        }

        addSeriousButton.setOnClickListener {
            setCategory(SERIOUS)
        }

        addCrazyButton.setOnClickListener {
            setCategory(CRAZY)
        }
    }

    private fun setCategory(selectedCategory: String) {
        this.selectedCategory = selectedCategory

        when (selectedCategory) {
            FUNNY -> {
                addFunnyButton.isChecked = true
                addSeriousButton.isChecked = false
                addCrazyButton.isChecked = false
            }
            SERIOUS -> {
                addFunnyButton.isChecked = false
                addSeriousButton.isChecked = true
                addCrazyButton.isChecked = false
            }
            CRAZY -> {
                addFunnyButton.isChecked = false
                addSeriousButton.isChecked = false
                addCrazyButton.isChecked = true
            }
        }
    }

    fun addPostClicked(view: View) {
        // add post to Firestore
        val data = HashMap<String, Any>()
        data[CATEGORY] = selectedCategory
        data[NUM_COMMENTS] = 0
        data[NUM_LIKES] = 0
        data[THOUGHT_TEXT] = addThoughtText.text.toString()
        data[TIMESTAMP] = FieldValue.serverTimestamp()
        data[USERNAME] = addUsernameText.text.toString()

        FirebaseFirestore.getInstance().collection(THOUGHT_REF)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not add post: $exception")
                }
    }
}
