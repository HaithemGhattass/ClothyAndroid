package com.clothy.clothyandroid.services

import retrofit2.Call
import retrofit2.http.*
data class ApiResponse(
    val doc: List<MatchResponse.Match>,
    val users: List<UserResponse.User>
)
interface MatchService {
    @PUT("match/swipe/{IdReciver}/{id}")
    fun match(
        @Path("IdReciver") IdReciver: String,@Body MatchRequest:MatchRequest): Call<MatchResponse>


    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("match/getmatchs")
    fun getmatchs(): Call<List<MatchResponse.Match>>

    @Headers("Content-Type: application/json; charset=utf-8")

    @GET("match/getmatch")
    fun getmatch(): Call<List<MatchResponse.Match>>
}