package com.example.books


import BookViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.books.model.BookDetail
import com.example.books.network.BookApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    private val viewModel: BookViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "main") {
                composable("main") {
                    MainScreen(viewModel, navController)
                }
                composable("detail/{bookTitle}") { backStackEntry ->
                    val bookTitle = backStackEntry.arguments?.getString("bookTitle") ?: ""
                    val book = viewModel.bookListStateFlow.value.find { it.bookTitle == bookTitle }
                    val context = LocalContext.current
                    book?.let {
                        DetailScreen(book, navController, context, viewModel)
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
        }
    }
}

@Composable
fun FinishedBooksScreen(viewModel: BookViewModel, navController: NavHostController) {
    val books by viewModel.bookListStateFlow.collectAsState()
    val finishedBooks = books
        .filter { viewModel.getBookState(it.bookTitle).value.isFinished }
        .distinctBy { it.bookTitle } // Filtriranje jedinstvenih knjiga po naslovu

    Scaffold(
        bottomBar = { NavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Finished Books",
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                items(finishedBooks) { book ->
                    BookItem(book = book, context = LocalContext.current) {
                        navController.navigate("detail/${book.bookTitle}")
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistBooksScreen(viewModel: BookViewModel, navController: NavHostController) {
    val books by viewModel.bookListStateFlow.collectAsState()
    val wishlistBooks = books
        .filter { viewModel.getBookState(it.bookTitle).value.isInWishlist }
        .distinctBy { it.bookTitle } // Filtriranje jedinstvenih knjiga po naslovu

    Scaffold(
        bottomBar = { NavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Wishlist Books",
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                items(wishlistBooks) { book ->
                    BookItem(book = book, context = LocalContext.current) {
                        navController.navigate("detail/${book.bookTitle}")
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingBooksScreen(viewModel: BookViewModel, navController: NavHostController) {
    val books by viewModel.bookListStateFlow.collectAsState()
    val readingBooks = books
        .filter { viewModel.getBookState(it.bookTitle).value.isReading }
        .distinctBy { it.bookTitle } // Filtriranje jedinstvenih knjiga po naslovu

    Scaffold(
        bottomBar = { NavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Reading Books",
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                items(readingBooks) { book ->
                    BookItem(book = book, context = LocalContext.current) {
                        navController.navigate("detail/${book.bookTitle}")
                    }
                }
            }
        }
    }
}



@ExperimentalCoroutinesApi
@Composable
fun MainScreen(viewModel: BookViewModel, navController: NavHostController) {
    Scaffold(
        bottomBar = { NavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var searchText by remember { mutableStateOf(TextFieldValue("")) }
            SearchBar(searchText) { newText ->
                searchText = newText
            }
            BookList(viewModel, searchText.text, navController)
        }
    }
}

@Composable
fun SearchBar(searchText: TextFieldValue, onSearchTextChange: (TextFieldValue) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
            .padding(8.dp)
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (searchText.text.isEmpty()) {
                    Text(
                        text = "Search",
                        style = TextStyle(color = Color.Gray, fontSize = 18.sp)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun BookList(viewModel: BookViewModel, filterText: String, navController: NavHostController) {
    val books by viewModel.bookListStateFlow.collectAsState()
    val uniqueBooks = books.distinctBy { it.bookTitle }
    val filteredBooks = uniqueBooks.filter {
        it.bookTitle.contains(filterText, ignoreCase = true)
    }
    val context = LocalContext.current

    LazyColumn {
        items(filteredBooks) { book ->
            BookItem(book = book, context = context) {
                navController.navigate("detail/${book.bookTitle}")
            }
        }
    }
}

@Composable
fun BookItem(book: BookDetail, context: Context, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(data = book.bookImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(465.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            //Text(text = book.bookTitle, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            //Text(text = "Author: ${book.bookAuthor}", style = TextStyle(fontSize = 16.sp))
            //Text(text = "Publisher: ${book.bookPublisher}", style = TextStyle(fontSize = 16.sp))
            //Text(text = "ISBN: ${book.bookIsbn}", style = TextStyle(fontSize = 16.sp))
            //Text(text = "Rank: ${book.bookRank}", style = TextStyle(fontSize = 16.sp))
            //Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazonBookUrl))
                context.startActivity(intent)
            })  {
                Text(text = "Buy on Amazon")
            }
        }
    }
}



@Composable
fun DetailScreen(
    book: BookDetail,
    navController: NavHostController,
    context: Context,
    viewModel: BookViewModel
) {
    val bookState by viewModel.getBookState(book.bookTitle).collectAsState()

    Scaffold(
        bottomBar = { NavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Image(
                    painter = rememberImagePainter(data = book.bookImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = book.bookTitle, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                Text(text = "Author: ${book.bookAuthor}", style = TextStyle(fontSize = 20.sp))
                Text(text = "Publisher: ${book.bookPublisher}", style = TextStyle(fontSize = 20.sp))
                Text(text = "ISBN: ${book.bookIsbn}", style = TextStyle(fontSize = 20.sp))
                Text(text = "Rank: ${book.bookRank}", style = TextStyle(fontSize = 20.sp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = book.bookDescription, style = TextStyle(fontSize = 16.sp))
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazonBookUrl))
                    context.startActivity(intent)
                }) {
                    Text(text = "Buy on Amazon")
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (bookState.isFinished) {
                    Button(onClick = {
                        viewModel.removeFromFinished(book.bookTitle)
                    }) {
                        Text(text = "Unfinish Book")
                    }
                } else {
                    Button(onClick = {
                        viewModel.markAsFinished(book.bookTitle)
                    }) {
                        Text(text = "Mark as Finished")
                    }
                }

                if (bookState.isInWishlist) {
                    Button(onClick = {
                        viewModel.removeFromWishlist(book.bookTitle)
                    }) {
                        Text(text = "Remove from Wishlist")
                    }
                } else {
                    Button(onClick = {
                        viewModel.addToWishlist(book.bookTitle)
                    }) {
                        Text(text = "Add to Wishlist")
                    }
                }

                if (bookState.isReading) {
                    Button(onClick = {
                        viewModel.removeFromReading(book.bookTitle)
                    }) {
                        Text(text = "Mark as Not Reading")
                    }
                } else {
                    Button(onClick = {
                        viewModel.markAsReading(book.bookTitle)
                    }) {
                        Text(text = "Mark as Reading")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    shareBookDetails(context, book)
                }) {
                    Text(text = "Share Book")
                }
            }
        }
    }
}

fun shareBookDetails(context: Context, book: BookDetail) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this book:\n\nTitle: ${book.bookTitle}\nAuthor: ${book.bookAuthor}\nPublisher: ${book.bookPublisher}\nISBN: ${book.bookIsbn}\nRank: ${book.bookRank}\n\n${book.bookDescription}\n\nFind it on Amazon: ${book.amazonBookUrl}")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share book via"))
}


@Composable
fun NavigationBar(navController: NavHostController?) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationItem(icon = painterResource(id = R.drawable.ic_books), label = "All Books", navController = navController, route = "main")
                NavigationItem(icon = painterResource(id = R.drawable.ic_finished), label = "Finished Books", navController = navController, route = "finishedBooks")
                NavigationItem(icon = painterResource(id = R.drawable.ic_wishlist), label = "Wishlist Books", navController = navController, route = "wishlistBooks")
                NavigationItem(icon = painterResource(id = R.drawable.ic_reading), label = "Reading Books", navController = navController, route = "readingBooks")
            }
        }
    )
}

@Composable
fun NavigationItem(icon: Painter, label: String, navController: NavHostController?, route: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                if (route.isNotEmpty() && navController != null) {
                    navController.navigate(route)
                }
            }
    ) {
        Icon(painter = icon, contentDescription = label)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label)
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    MainScreen(BookViewModel(), navController)
}