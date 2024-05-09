package com.example.hw5

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PostDetailScreen(postId: Int, posts:  MutableList<Post>, onReplyAdded: (Int, String) -> Unit, navController: NavController) {
    val post = posts.find { it.id == postId } ?: return

    Column {
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
        Text(text = post.title, style = MaterialTheme.typography.headlineMedium)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.image)
                .crossfade(true)
                .build(),
            contentDescription = post.title
        )
        Text(text = "By ${post.author}")
        Text(text = post.content)
        var reply by remember { mutableStateOf("") }
        TextField(
            value = reply,
            onValueChange = { reply = it },
            label = { Text("Your reply") }
        )

        Button(onClick = {
            if (reply.isNotBlank()) {
                onReplyAdded(postId, reply)
                navController.popBackStack()
//                    reply = ""

            }
        }) {
            Text("Reply")
        }
    }
}