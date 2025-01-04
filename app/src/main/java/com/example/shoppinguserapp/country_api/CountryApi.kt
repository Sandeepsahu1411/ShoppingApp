package com.example.shoppinguserapp.country_api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class CountryResponse(
    val name: Name,
    val flags: Flags
)

data class Name(
    val common: String
)

data class Flags(
    val png: String
)

// Retrofit interface
interface CountryApi {
    @GET("all")
    suspend fun getAllCountries(): List<CountryResponse>
}

// Retrofit instance
object RetrofitInstance {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    val api: CountryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }
}
