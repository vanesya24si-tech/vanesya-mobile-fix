package com.example.nesa_drunk.api

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    val success: Boolean,
    val message: String?,
    val data: NewsData?
)

data class NewsData(
    val link: String?,
    val image: String?,
    val description: String?,
    val title: String?,
    val posts: List<NewsItem>?
)

data class NewsItem(
    val link: String,
    val title: String,
    @SerializedName("pubDate") val date: String?,
    val description: String?,
    val thumbnail: String?
)
