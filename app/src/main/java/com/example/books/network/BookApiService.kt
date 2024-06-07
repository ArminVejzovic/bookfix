package com.example.books.network

import com.example.books.model.BookDetail
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("getBooks")
    suspend fun getBooksList(
        @Query("rapidapi-key") apiKey: String = API_KEY
    ): List<BookDetail>

    companion object {
        private const val BASE_URL = "https://all-books-api.p.rapidapi.com/"
        const val API_KEY = "dc5979f7c6mshb1ab68ea88597f0p1c0c0cjsn500514408b17"

        fun provideBookApi(): BookApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookApiService::class.java)
        }
    }
}
