package com.example.shitakemura.androidfirestore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ThoughtsAdapter(val thoughts: ArrayList<Thought>): RecyclerView.Adapter<ThoughtsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.thought_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = thoughts.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bindThought(thoughts[position])
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val username = itemView?.findViewById<TextView>(R.id.listViewUsername)
        val timestamp = itemView?.findViewById<TextView>(R.id.listViewTimestamp)
        val thoughtText = itemView?.findViewById<TextView>(R.id.listViewThoughtText)
        val likesImage = itemView?.findViewById<ImageView>(R.id.listVIewLikesImage)
        val numLikes = itemView?.findViewById<TextView>(R.id.listVIewLikesImage)

        fun bindThought(thought: Thought) {
            username?.text = thought.username
            thoughtText?.text = thought.thoughtText
            numLikes?.text = thought.numLikes.toString()

            val dateFormatter = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
            val dateString = dateFormatter.format(thought.timestamp)
            timestamp?.text = dateString
        }
    }


}