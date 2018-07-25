package com.example.shitakemura.androidfirestore.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.shitakemura.androidfirestore.Adapters.ThoughtsAdapter
import com.example.shitakemura.androidfirestore.Model.Thought
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var selectedCategory = FUNNY
    private lateinit var thoughtsAdapter: ThoughtsAdapter
    private val thoughts = arrayListOf<Thought>()
    private val thoughtsCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHT_REF)
    private lateinit var thoughtsListener: ListenerRegistration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val addThoughtIntent = Intent(this, AddThoughtActivity::class.java)
            startActivity(addThoughtIntent)
        }

        mainFunnyButton.setOnClickListener {
            setCategory(FUNNY)
            resetListener()
        }

        mainSeriousButton.setOnClickListener {
            setCategory(SERIOUS)
            resetListener()
        }

        mainCrazyButton.setOnClickListener {
            setCategory(CRAZY)
            resetListener()
        }

        mainPopularButton.setOnClickListener {
            setCategory(POPULAR)
            resetListener()
        }

        thoughtsAdapter = ThoughtsAdapter(thoughts) { thought ->
            val commentsActivity = Intent(this, CommentsActivity::class.java)
            commentsActivity.putExtra(DOCUMENT_KEY, thought.documentId)
            startActivity(commentsActivity)
        }
        thoughtListView.adapter = thoughtsAdapter

        val layoutManager = LinearLayoutManager(this)
        thoughtListView.layoutManager = layoutManager
        auth = FirebaseAuth.getInstance()
    }

    private fun setCategory(selectedCategory: String) {
        this.selectedCategory = selectedCategory
        uncheckedToggleButtons()

        when (selectedCategory) {
            FUNNY -> {
                mainFunnyButton.isChecked = true
            }
            SERIOUS -> {
                mainSeriousButton.isChecked = true
            }
            CRAZY -> {
                mainCrazyButton.isChecked = true
            }
            POPULAR -> {
                mainPopularButton.isChecked = true
            }
        }
    }

    private fun uncheckedToggleButtons() {
        mainFunnyButton.isChecked = false
        mainSeriousButton.isChecked = false
        mainCrazyButton.isChecked = false
        mainPopularButton.isChecked = false
    }

    private fun resetListener() {
        thoughtsListener.remove()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.getItem(0)
        if (auth.currentUser == null) {
            menuItem.title = "Login"
        } else {
            menuItem.title = "Logout"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun updateUI() {
        if (auth.currentUser == null) {
            setButtonsEnabled(false)
            thoughts.clear()
            thoughtsAdapter.notifyDataSetChanged()

        } else {
            setButtonsEnabled(true)
            setListener()
        }
    }

    private fun setButtonsEnabled(isEnabled: Boolean) {
        mainFunnyButton.isEnabled = isEnabled
        mainSeriousButton.isEnabled = isEnabled
        mainCrazyButton.isEnabled = isEnabled
        mainPopularButton.isEnabled = isEnabled
        fab.isEnabled = isEnabled
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_login) {
            if (auth.currentUser == null) {
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                auth.signOut()
                updateUI()
            }
            return true
        }
        return false
    }

    private fun setListener() {
        if (selectedCategory == POPULAR) {

            thoughtsListener = thoughtsCollectionRef
                    .orderBy(NUM_LIKES, Query.Direction.DESCENDING)
                    .addSnapshotListener(this) { snapshot, exception ->
                        if (exception != null) {
                            Log.e("Exception", "Could not retrieve documents: $exception")
                        }

                        if (snapshot != null) {
                            parseData(snapshot)
                        }
                    }
        } else {
            thoughtsListener = thoughtsCollectionRef
                    .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(CATEGORY, selectedCategory)
                    .addSnapshotListener(this) { snapshot, exception ->
                        if (exception != null) {
                            Log.e("Exception", "Could not retrieve documents: $exception")
                        }

                        if (snapshot != null) {
                            parseData(snapshot)
                        }
                    }
        }

    }

    private fun parseData(snapshot: QuerySnapshot) {
        thoughts.clear()

        for (document in snapshot.documents) {
            val data = document.data
            data?.let {
                val name = data[USERNAME] as String
                val timestamp = data[TIMESTAMP] as Date
                val thoughtText = data[THOUGHT_TEXT] as String
                val numLikes = data[NUM_LIKES] as Long
                val numComments = data[NUM_COMMENTS] as Long
                val documentId = document.id

                val newThought = Thought(name, timestamp, thoughtText, numLikes.toInt(), numComments.toInt(), documentId)
                thoughts.add(newThought)
            }
        }
        thoughtsAdapter.notifyDataSetChanged()
    }
}
