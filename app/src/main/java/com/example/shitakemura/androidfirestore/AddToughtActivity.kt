package com.example.shitakemura.androidfirestore

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_tought.*

class AddToughtActivity : AppCompatActivity() {

    var selectedCategory = FUNNY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tought)
    }

    fun addPostClicked(view: View) {
        // add post to Firestore
        val data = HashMap<String, Any>()
        data.put("category", selectedCategory)
        data.put("numComments", 0)
        data.put("numLikes", 0)
        data.put("thoughtText", addToughtText.text.toString())
        data.put("timestamp", FieldValue.serverTimestamp())
        data.put("username", addUsernameText.text.toString())

        FirebaseFirestore.getInstance().collection(THOUGHT_REF)
                .add(data)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener { exception ->  
                    Log.e("Exception", "Could not add post: $exception")
                }

    }


    fun addFunnyClicked(view: View) {
        selectedCategory = FUNNY
        addFunnyButton.isChecked = true
        addSeriousButton.isChecked = false
        addCrazyButton.isChecked = false
    }

    fun addSeriousClicked(view: View) {
        selectedCategory = SERIOUS
        addFunnyButton.isChecked = false
        addSeriousButton.isChecked = true
        addCrazyButton.isChecked = false
    }

    fun addCrazyClicked(view: View) {
        selectedCategory = CRAZY
        addFunnyButton.isChecked = false
        addSeriousButton.isChecked = false
        addCrazyButton.isChecked = true
    }
}
