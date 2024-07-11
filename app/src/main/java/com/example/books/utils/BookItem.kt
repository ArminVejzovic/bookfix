package com.example.books.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.books.model.BookDetail
import com.example.books.R

@Composable
fun BookItem(book: BookDetail, context: Context, onClick: () -> Unit) {
    val isTablet = isTablet()
    val imageSize = if (isTablet) 600.dp else 465.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(data = book.bookImage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageSize),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(book.amazonBookUrl))
                context.startActivity(intent)
            }) {
                Text(text = "Buy on Amazon")
            }
        }
    }
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    return screenWidthDp >= 600
}