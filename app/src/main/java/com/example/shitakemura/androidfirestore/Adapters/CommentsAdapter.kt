package com.example.shitakemura.androidfirestore.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.shitakemura.androidfirestore.Model.Comment
import com.example.shitakemura.androidfirestore.R
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(private val comments: ArrayList<Comment>): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return comments.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComment(comments[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_view, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {

        val username = itemView?.findViewById<TextView>(R.id.commentListUsername)
        private val timestamp = itemView?.findViewById<TextView>(R.id.commentListTimestamp)
        private val commentText = itemView?.findViewById<TextView>(R.id.commentListCommentText)

        fun bindComment(comment: Comment) {
            username?.text = comment.username
            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(comment.timestamp)
            timestamp?.text = dateString
            commentText?.text = comment.commentText
        }
    }
}