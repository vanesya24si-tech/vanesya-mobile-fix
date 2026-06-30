package com.example.nesa_drunk.api

import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApiService {
    @GET("{publisher}/{category}")
    suspend fun getNews(
        @Path("publisher") publisher: String,
        @Path("category") category: String
    ): NewsResponse
}
