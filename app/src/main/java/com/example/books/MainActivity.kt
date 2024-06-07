package com.example.books


import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.books.ui.screen.MainScreen
import com.example.books.ui.screen.NasDetailScreen
import com.example.books.viewmodel.BookViewModel
import com.example.books.viewmodel.FinishedBooksScreen
import com.example.books.viewmodel.ReadingBooksScreen
import com.example.books.viewmodel.WishlistBooksScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    private val viewModel: BookViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            if (isInternetAvailable(context)) {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(viewModel, navController)
                    }
                    composable("detail/{bookTitle}") { backStackEntry ->
                        val bookTitle = backStackEntry.arguments?.getString("bookTitle") ?: ""
                        val book = viewModel.bookListStateFlow.value.find { it.bookTitle == bookTitle }
                        book?.let {
                            NasDetailScreen(book, navController, context, viewModel)
                        }
                    }
                    composable("finishedBooks") {
                        FinishedBooksScreen(viewModel, navController)
                    }
                    composable("wishlistBooks") {
                        WishlistBooksScreen(viewModel, navController)
                    }
                    composable("readingBooks") {
                        ReadingBooksScreen(viewModel, navController)
                    }
                }
            } else {
                NoConnectionScreen()
            }
        }
    }
}


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

@Composable
fun NoConnectionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_connection),
            contentDescription = "No Connection",
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Internet Connection",
            style = TextStyle(fontSize = 24.sp)
        )
    }
}