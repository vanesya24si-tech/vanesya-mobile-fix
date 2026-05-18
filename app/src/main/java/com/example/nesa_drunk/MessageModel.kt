package com.example.nesa_drunk

data class MessageModel(
    val senderName: String,
    val messageText: String,
    val avatarUrl: String,
    val contentImageUrl: String? = null,
    val date: String = "Baru saja"
)
