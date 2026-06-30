package com.example.nesa_drunk.database

typealias FavoriteNews = FavoriteNewsItem
typealias AgendaDesa = AgendaItem
typealias CatatanWarga = CatatanItem

data class FavoriteNewsItem(
    val id: Int = 0,
    val title: String,
    val image: String? = null,
    val author: String? = null,
    val date: String? = null,
    val description: String? = null,
    val link: String? = null
)
