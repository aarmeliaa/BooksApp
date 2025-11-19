package com.example.booksapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    BooksScreen()
                }
            }
        }
    }
}

@Composable
fun BooksScreen(vm: BooksViewModel = viewModel()) {
    val context = LocalContext.current
    val results by vm.results.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Nama buku") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { vm.search(query) }) {
                Text("Cari")
            }
        }
        Spacer(Modifier.height(12.dp))
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        error?.let { err ->
            Text(
                text = err,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results, key = { it.id }) { item ->
                BookRow(
                    item = item,
                    onClick = {
                        val vi = item.volumeInfo
                        val authors = vi.authors?.joinToString(", ") ?: "-"
                        val cover = secureImageUrl(
                            vi.imageLinks?.thumbnail ?:
                            vi.imageLinks?.smallThumbnail
                        )
                        val intent = Intent(context,
                            DetailActivity::class.java).apply {
                            putExtra("title", vi.title ?: "-")
                            putExtra("authors", authors)
                            putExtra("cover", cover ?: "")
                            putExtra("description", vi.description ?: "-")
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun BookRow(item: BookItem, onClick: () -> Unit) {
    val vi = item.volumeInfo
    val authors = vi.authors?.joinToString(", ") ?: "-"
    val cover = secureImageUrl(
        vi.imageLinks?.thumbnail ?: vi.imageLinks?.smallThumbnail
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cover,
            contentDescription = "Cover",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.LightGray)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(vi.title ?: "-", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(authors, style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun secureImageUrl(url: String?): String? = url?.replace("http://",
    "https://")