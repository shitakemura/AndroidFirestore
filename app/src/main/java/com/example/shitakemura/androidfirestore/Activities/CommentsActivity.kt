package com.example.shitakemura.androidfirestore.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.DOCUMENT_KEY
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {
    lateinit var thoughtDocumentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
        println("thoughtDocumentId: $thoughtDocumentId")

        addCommentButton.setOnClickListener {
            addComment()
        }
    }

    private fun addComment() {
        
    }
}
