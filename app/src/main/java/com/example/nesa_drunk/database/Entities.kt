package com.example.nesa_drunk.database

typealias FavoriteNews = FavoriteNewsItem
typealias AgendaDesa = AgendaItem
typealias CatatanWarga = CatatanItem

data class AgendaItem(
    val id: Int = 0,
    val kegiatan: String,
    val tanggal: String,
    val waktu: String,
    val lokasi: String,
    val status: String = "Akan Datang"
)

data class CatatanItem(
    val id: Int = 0,
    val judul: String,
    val isi: String,
    val tanggal: String
)

data class InfoItem(
    val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val avatar: String,
    val imageUrl: String,
    val kategori: String,
    val tanggal: String
)

data class FavoriteNewsItem(
    val id: Int = 0,
    val title: String,
    val image: String? = null,
    val author: String? = null,
    val date: String? = null,
    val description: String? = null,
    val link: String? = null
)
