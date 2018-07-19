package com.example.shitakemura.androidfirestore.Model

import java.util.*

data class Thought(val username: String, val timestamp: Date, val thoughtText: String,
                   val numLikes: Int, val numComments: Int, val documentId: String)