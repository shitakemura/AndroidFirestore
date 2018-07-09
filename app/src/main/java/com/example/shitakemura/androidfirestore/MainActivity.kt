package com.example.shitakemura.androidfirestore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var selectedCategory = FUNNY
    private lateinit var thoughtsAdapter: ThoughtsAdapter
    private val thoughts = arrayListOf<Thought>()
    private val thoughtsCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHT_REF)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val addToughtIntent = Intent(this, AddToughtActivity::class.java)
            startActivity(addToughtIntent)
        }

        thoughtsAdapter = ThoughtsAdapter(thoughts)
        thoughtListView.adapter = thoughtsAdapter

        val layoutManager = LinearLayoutManager(this)
        thoughtListView.layoutManager = layoutManager

        thoughtsCollectionRef
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val data = document.data
                        data?.let { data ->
                            val name = data[USERNAME] as String
                            val timestamp = data[TIMESTAMP] as Date
                            val thoughtText = data[THOUGHT_TEXT] as String
                            val numLikes = data[NUM_LIKES] as Long
                            val numComments = data[NUM_LIKES] as Long
                            val documentId = document.id

                            val newThought = Thought(name, timestamp, thoughtText, numLikes.toInt(), numComments.toInt(), documentId)
                            thoughts.add(newThought)
                        }
                    }
                    thoughtsAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception", "Could not get thoughts: $exception")
                }

    }

    fun mainFunnyClicked(view: View) {
        selectedCategory = FUNNY
        mainFunnyButton.isChecked = true
        mainSeriousButton.isChecked = false
        mainCrazyButton.isChecked = false
        mainPopularButton.isChecked = false
    }

    fun mainSeriousClicked(view: View) {
        selectedCategory = SERIOUS
        mainFunnyButton.isChecked = false
        mainSeriousButton.isChecked = true
        mainCrazyButton.isChecked = false
        mainPopularButton.isChecked = false
    }

    fun mainCrazyClicked(view: View) {
        selectedCategory = CRAZY
        mainFunnyButton.isChecked = false
        mainSeriousButton.isChecked = false
        mainCrazyButton.isChecked = true
        mainPopularButton.isChecked = false
    }

    fun mainPopularClicked(view: View) {
        selectedCategory = POPULAR
        mainFunnyButton.isChecked = false
        mainSeriousButton.isChecked = false
        mainCrazyButton.isChecked = false
        mainPopularButton.isChecked = true
    }
}
