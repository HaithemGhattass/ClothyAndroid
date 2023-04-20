package com.clothy.clothyandroid.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MatchService {
    @PUT("match/swipe/{IdReciver}")
    fun match(
        @Path("IdReciver") IdReciver: String): Call<MatchResponse>
}