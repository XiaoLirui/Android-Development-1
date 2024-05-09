package com.example.hw5

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileScreen(posts:  MutableList<Post>) {
    LazyColumn {
        items(posts) { post ->
            if (post.replies.isNotEmpty()) {
                Text(text = "Replies to ${post.title}:")
                post.replies.forEach { reply ->
                    Text(text = reply)
                }
            }
        }
    }
}