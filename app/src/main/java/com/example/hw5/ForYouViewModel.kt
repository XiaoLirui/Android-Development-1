package com.example.hw5
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


class ForYouViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val jsonAdapter = moshi.adapter(DogApiResponse::class.java)

    var images: MutableList<String> = mutableListOf()
        private set

    init {
        fetchImages()
    }

    private fun fetchImages() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            run {
                val request = Request.Builder()
                    .url("https://dog.ceo/api/breeds/image/random/10")
                    .build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.string()?.let {
                            val apiResponse = jsonAdapter.fromJson(it)
                            apiResponse?.message?.let { imageUrls ->
                                Log.d("ForYouViewModel", "Fetched image URLs: $imageUrls")
//                                images.add(imageUrls.toString())
                                withContext(Dispatchers.Main) {
                                    images.addAll(imageUrls)
                                }
                            }
                        }
                    } else {
                        Log.e("ForYouViewModel", "Failed to fetch images: ${response.message}")
                    }
                }
            }
        }
    }
}

    @Composable
    fun ForYouScreen( navController: NavController) {
        val viewModel: ForYouViewModel = viewModel()
        Scaffold(
            bottomBar = { BottomNavBar(navController) }
        ){ innerPadding ->
            ImageList(viewModel, innerPadding)
        }
    }

    @Composable
    fun PostComponent(imageUrl: String) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Posted image",
            )
    }


@Composable
fun ImageList( viewModel: ForYouViewModel, innerPadding: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(viewModel.images) { imageUrl ->
            PostComponent(imageUrl)
        }
    }
}
