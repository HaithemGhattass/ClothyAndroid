package com.clothy.clothyandroid.services

import retrofit2.Call
import retrofit2.http.GET

interface OutfitService {
    @GET("outfit/getall")
    fun getoutfit(): Call<List<OutfitResponse.Outfit>>
}