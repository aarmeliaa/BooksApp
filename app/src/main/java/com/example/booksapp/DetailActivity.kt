package com.example.booksapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title") ?: "-"
        val authors = intent.getStringExtra("authors") ?: "-"
        val cover = intent.getStringExtra("cover") ?: ""
        val description = intent.getStringExtra("description") ?: "-"
        setContent {
            MaterialTheme {
                Surface {
                    DetailScreen(title, authors, cover, description)
                }
            }
        }
    }
}

@Composable
fun  DetailScreen(
    title: String,
    authors: String,
    cover: String,
    description: String
) {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = cover.ifEmpty { null },
            contentDescription = "Cover",
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        )

        Spacer(Modifier.height(16.dp))

        Text(title, style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(authors, style = MaterialTheme.typography.	bodyLarge, color =
            Color.Gray)

        Spacer(Modifier.height(16.dp))
        Text("Deskripsi", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Text(description.ifBlank { "-" }, style = MaterialTheme.typography.
        bodyLarge)
    }
}