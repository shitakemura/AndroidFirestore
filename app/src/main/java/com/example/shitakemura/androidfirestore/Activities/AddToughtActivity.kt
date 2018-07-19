package com.example.shitakemura.androidfirestore.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.shitakemura.androidfirestore.*
import com.example.shitakemura.androidfirestore.Utilities.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_tought.*

class AddToughtActivity : AppCompatActivity() {

    private var selectedCategory = FUNNY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tought)

        addFunnyButton.setOnClickListener {
            selectedCategory = FUNNY
            addFunnyButton.isChecked = true
            addSeriousButton.isChecked = false
            addCrazyButton.isChecked = false
        }

        addSeriousButton.setOnClickListener {
            selectedCategory = SERIOUS
            addFunnyButton.isChecked = false
            addSeriousButton.isChecked = true
            addCrazyButton.isChecked = false
        }

        addCrazyButton.setOnClickListener {
            selectedCategory = CRAZY
            addFunnyButton.isChecked = false
            addSeriousButton.isChecked = false
            addCrazyButton.isChecked = true
        }
    }

    fun addPostClicked(view: View) {
        // add post to Firestore
        val data = HashMap<String, Any>()
        data.put(CATEGORY, selectedCategory)
        data.put(NUM_COMMENTS, 0)
        data.put(NUM_LIKES, 0)
        data.put(THOUGHT_TEXT, addToughtText.text.toString())
        data.put(TIMESTAMP, FieldValue.serverTimestamp())
        data.put(USERNAME, addUsernameText.text.toString())

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
