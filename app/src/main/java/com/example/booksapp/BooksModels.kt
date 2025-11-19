package com.example.booksapp

data class BooksResponse(
    val items: List<BookItem>?
)
data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null
)
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)
