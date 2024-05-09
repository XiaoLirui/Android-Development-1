package com.example.hw5
import coil.compose.AsyncImage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.request.ImageRequest
import com.example.hw5.ui.theme.HW5Theme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HW5Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator(posts = samplePosts, onReplyAdded = { postId, reply ->
                        onReplyAdded(postId, reply, samplePosts)
                    })
            }
        }
    }
}
}



@Composable
fun MainScreen(navController: NavController, posts:  MutableList<Post>) {

    Scaffold(
        bottomBar = { BottomNavBar(navController) }

    ){ innerPadding ->
        PostsList(posts, navController, innerPadding)
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = navController.currentDestination?.route == "mainScreen",
            onClick = { navController.navigate("mainScreen") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = "For You") },
            label = { Text("For You") },
            selected = navController.currentDestination?.route == "forYou",
            onClick = {
                navController.navigate("forYou") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}


@Composable
fun PostsList(posts: List<Post>, navController: NavController, innerPadding: PaddingValues) {
    LazyColumn {
        items(posts) { post ->
            PostCell(post = post) {
                navController.navigate("postDetail/${post.id}")
            }
        }
    }
}


@Composable
fun PostCell(post: Post, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp).clickable(onClick = onClick)) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.image)
                    .crossfade(true)
                    .build(),
                contentDescription = post.title,
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = post.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Author: ${post.author}")
            Text(text = post.content, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (post.replies.isNotEmpty()) {
                Text(text = "Replies:", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red), modifier = Modifier.padding(top = 4.dp))
                post.replies.forEach { reply ->
                    Text(text = reply, style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red))
                }
            }
        }
    }
}





fun onReplyAdded(postId: Int, reply: String, posts: MutableList<Post>) {
    val post = posts.find { it.id == postId }
    post?.replies?.add(reply)
}



    @Composable
    fun AppNavigator(posts:  MutableList<Post>, onReplyAdded: (Int, String) -> Unit) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "mainScreen") {
            composable("mainScreen") {
                MainScreen(navController, posts)
            }
            composable("forYou") {
                ForYouScreen(navController)
//                ForYouScreen(ForYouViewModel,navController)
            }
            composable("postDetail/{postId}") { backStackEntry ->
                val postId = backStackEntry.arguments?.getString("postId")?.toInt() ?: 0
                PostDetailScreen(
                    postId = postId,
                    posts = posts,
                    onReplyAdded = onReplyAdded,
                    navController = navController
                )
            }
            composable("profileScreen") {
                ProfileScreen(posts)
            }
        }
    }



data class Post(
    val id: Int,
    val image: String,
    val title: String,
    val author: String,
    val content: String,
    val replies: MutableList<String> = mutableListOf()
)

val samplePosts = mutableStateListOf(
    Post(1, "https://www.billboard.com/wp-content/uploads/2023/07/aretha-franklin-young-gifted-black-1972-billboard-1240.jpg?w=1024", "Post 1", "Alice", "Hello there, this is my first post!",  mutableListOf("Welcome, Alice!")),
    Post(2, "https://www.billboard.com/wp-content/uploads/2022/06/beyonce-Lemonade-album-art-billboard-1240.jpg?w=1024", "Post 2", "Bob", "Welcome to the second post!",mutableListOf("hh")),
    Post(3, "https://www.billboard.com/wp-content/uploads/media/ariana-grande-sweetner-album-art-2018-billboard-1240.jpg?w=1024", "Post 3", "Bee", "A nice day at Cornell",mutableListOf("Glad to hear that!")),
    Post(4, "https://www.billboard.com/wp-content/uploads/2023/07/asap-rocky-long-live-asap-2013-billboard-1240.jpg?w=1024", "Post 4", "Cristian", "The Slope Day!",mutableListOf("I will come!")),
    Post(5, "https://www.billboard.com/wp-content/uploads/2023/07/lil-kim-hard-core-1996-billboard-1240.jpg?w=1037", "Post 5", "Jeff", "Strong Wind!",mutableListOf("sad")),
    Post(6, "https://www.billboard.com/wp-content/uploads/2023/07/gloria-estefan-mi-tierra-cover-1993-billboard-1240.jpg?w=1024", "Post 6", "Daniel", "I am tired",mutableListOf("me too")),
    Post(7, "https://www.billboard.com/wp-content/uploads/2023/07/fela-kuti-no-agreement-1977-billboard-1240.jpg?w=1024", "Post 7", "Rishi", "Got a job offer!",mutableListOf("congrats!")),
    Post(8, "https://www.billboard.com/wp-content/uploads/2023/07/roberta-flack-first-take-cover-1969-billboard-1240.jpg?w=1024", "Post 8", "Raymond", "The scene in Ithaca",mutableListOf("hh")),
    Post(9, "https://www.billboard.com/wp-content/uploads/2023/07/carole-king-tapestry-1971-billboard-1240.jpg?w=1240", "Post 9", "Jonathan", "Want feedback for CS 1996",mutableListOf("Good")),
    Post(10, "https://www.billboard.com/wp-content/uploads/2023/07/rosanne-cash-kings-record-shop-1987-billboard-1240.jpg?w=1240", "Post 10", "Becky", "Want feedback for CS 1998!",mutableListOf("Perfect")),
)



