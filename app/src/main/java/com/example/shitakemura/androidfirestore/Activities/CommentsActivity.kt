package com.example.shitakemura.androidfirestore.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.shitakemura.androidfirestore.Adapters.CommentsAdapter
import com.example.shitakemura.androidfirestore.Model.Comment
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {
    private lateinit var thoughtDocumentId: String
    private lateinit var commentsAdapter: CommentsAdapter
    private val comments = arrayListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId = intent.getStringExtra(DOCUMENT_KEY)
        println("thoughtDocumentId: $thoughtDocumentId")

        commentsAdapter = CommentsAdapter(comments)
        commentListView.adapter = commentsAdapter
        commentListView.layoutManager = LinearLayoutManager(this)

        addCommentButton.setOnClickListener {
            addComment()
        }
    }

    private fun addComment() {
        val commentText = enterCommentText.text.toString()
        val thoughtRef = FirebaseFirestore.getInstance().collection(THOUGHT_REF).document(thoughtDocumentId)

        FirebaseFirestore.getInstance().runTransaction { transaction ->

            val thought = transaction.get(thoughtRef)
            val numComments = thought.getLong(NUM_COMMENTS) ?: 0 + 1
            transaction.update(thoughtRef, NUM_COMMENTS, numComments)

            val newCommentRef = FirebaseFirestore.getInstance().collection(THOUGHT_REF)
                    .document(thoughtDocumentId).collection(COMMENTS_REF).document()

            val data = HashMap<String, Any>()
            data[COMMENT_TEXT] = commentText
            data[TIMESTAMP] = FieldValue.serverTimestamp()
            data[USERNAME] = FirebaseAuth.getInstance().currentUser?.displayName.toString()

            transaction.set(newCommentRef, data)
        }
                .addOnSuccessListener {
                    enterCommentText.setText("")
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception,", "Could not add comment ${exception.localizedMessage}")
                }
    }


}
