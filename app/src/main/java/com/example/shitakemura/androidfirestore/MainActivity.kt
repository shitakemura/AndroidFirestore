package com.example.shitakemura.androidfirestore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    lateinit var thoughtsListener: ListenerRegistration
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val addToughtIntent = Intent(this, AddToughtActivity::class.java)
            startActivity(addToughtIntent)
        }

        mainFunnyButton.setOnClickListener {
            selectedCategory = FUNNY
            mainFunnyButton.isChecked = true
            mainSeriousButton.isChecked = false
            mainCrazyButton.isChecked = false
            mainPopularButton.isChecked = false

            thoughtsListener.remove()
            setListener()
        }

        mainSeriousButton.setOnClickListener {
            selectedCategory = SERIOUS
            mainFunnyButton.isChecked = false
            mainSeriousButton.isChecked = true
            mainCrazyButton.isChecked = false
            mainPopularButton.isChecked = false

            thoughtsListener.remove()
            setListener()
        }

        mainCrazyButton.setOnClickListener {
            selectedCategory = SERIOUS
            mainFunnyButton.isChecked = false
            mainSeriousButton.isChecked = false
            mainCrazyButton.isChecked = true
            mainPopularButton.isChecked = false

            thoughtsListener.remove()
            setListener()
        }

        mainPopularButton.setOnClickListener {
            selectedCategory = SERIOUS
            mainFunnyButton.isChecked = false
            mainSeriousButton.isChecked = false
            mainCrazyButton.isChecked = false
            mainPopularButton.isChecked = true

            thoughtsListener.remove()
            setListener()
        }

        thoughtsAdapter = ThoughtsAdapter(thoughts)
        thoughtListView.adapter = thoughtsAdapter

        val layoutManager = LinearLayoutManager(this)
        thoughtListView.layoutManager = layoutManager
        auth = FirebaseAuth.getInstance()
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

    fun updateUI() {
        if (auth.currentUser == null) {
            mainCrazyButton.isEnabled = false
            mainPopularButton.isEnabled = false
            mainFunnyButton.isEnabled = false
            mainSeriousButton.isEnabled = false
            fab.isEnabled = false
            thoughts.clear()
            thoughtsAdapter.notifyDataSetChanged()
        } else {
            mainCrazyButton.isEnabled = true
            mainPopularButton.isEnabled = true
            mainFunnyButton.isEnabled = true
            mainSeriousButton.isEnabled = true
            fab.isEnabled = true
            setListener()
        }
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

    fun setListener() {

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

    fun parseData(snapshot: QuerySnapshot) {
        thoughts.clear()

        for (document in snapshot.documents) {
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
}
