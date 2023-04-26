package com.clothy.clothyandroid.services

import retrofit2.Call
import retrofit2.http.*

interface OutfitService {
    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("outfit/allOFT")
    fun getoutfit(): Call<List<OutfitResponse.Outfit>>
}