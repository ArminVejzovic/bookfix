package com.example.books.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.books.R

@Composable
fun NasNavigationBar(navController: NavHostController?) {
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
