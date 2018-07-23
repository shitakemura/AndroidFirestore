package com.example.shitakemura.androidfirestore.Activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.example.shitakemura.androidfirestore.Adapters.CommentsAdapter
import com.example.shitakemura.androidfirestore.Model.Comment
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_comments.*
import java.util.*

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

        FirebaseFirestore.getInstance().collection(THOUGHT_REF).document(thoughtDocumentId)
                .collection(COMMENTS_REF).addSnapshotListener { snapshot, exception ->

                    if (exception != null) {
                        Log.e("Exception,", "Could not retrieve comments ${exception.localizedMessage}")
                    }

                    if (snapshot != null) {
                        comments.clear()

                        for (document in snapshot.documents) {
                            val data = document.data

                            data?.let {
                                val name = data[USERNAME] as String
                                val timestamp = data[TIMESTAMP] as Date
                                val commentText = data[COMMENT_TEXT] as String

                                val newComment = Comment(name, timestamp, commentText)
                                comments.add(newComment)
                            }
                        }
                        commentsAdapter.notifyDataSetChanged()
                    }
                }

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
                    hideKeyboard()
                }
                .addOnFailureListener { exception ->
                    Log.e("Exception,", "Could not add comment ${exception.localizedMessage}")
                }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
