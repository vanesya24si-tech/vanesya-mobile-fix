package com.example.nesa_drunk

data class MessageModel(
    val senderName: String,
    val messageText: String,
    val avatarUrl: String,
    val contentImageUrl: String? = null,
    val date: String = "Baru saja",
    val type: MessageType = MessageType.PESAN
)

enum class MessageType {
    PENGUMUMAN, PESAN
}
