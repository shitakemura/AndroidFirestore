package com.example.shitakemura.androidfirestore.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shitakemura.androidfirestore.Model.Thought
import com.example.shitakemura.androidfirestore.R
import com.example.shitakemura.androidfirestore.Utilities.NUM_LIKES
import com.example.shitakemura.androidfirestore.Utilities.THOUGHT_REF
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ThoughtsAdapter(val thoughts: ArrayList<Thought>, val itemClick: (Thought) -> Unit): RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.thought_list_view, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount() = thoughts.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindThought(thoughts[position])
    }

    inner class ViewHolder(itemView: View?, val itemClick: (Thought) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val username = itemView?.findViewById<TextView>(R.id.listViewUsername)
        val timestamp = itemView?.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtText = itemView?.findViewById<TextView>(R.id.listViewThoughtText)
        val likesImage = itemView?.findViewById<ImageView>(R.id.listVIewLikesImage)
        val numLikes = itemView?.findViewById<TextView>(R.id.listViewNumLikesLabel)

        fun bindThought(thought: Thought) {
            username?.text = thought.username
            thoughtText?.text = thought.thoughtText
            numLikes?.text = thought.numLikes.toString()

            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(thought.timestamp)
            timestamp?.text = dateString
            itemView.setOnClickListener { itemClick(thought) }

            likesImage?.setOnClickListener {
                FirebaseFirestore.getInstance().collection(THOUGHT_REF).document(thought.documentId)
                        .update(NUM_LIKES, thought.numLikes + 1)
            }
        }
    }
}