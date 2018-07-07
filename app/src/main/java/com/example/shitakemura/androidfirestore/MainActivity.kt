package com.example.shitakemura.androidfirestore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var selectedCategory = FUNNY
    lateinit var thoughtsAdapter: ThoughtsAdapter
    val thoughts = arrayListOf<Thought>()

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
