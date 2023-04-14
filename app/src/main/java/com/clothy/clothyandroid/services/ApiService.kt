package com.clothy.clothyandroid.services

import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @POST("login")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @POST("register")
    fun register(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @GET("allUser")
    fun getUsers(): Call<List<UserResponse.User>>
    @PUT("updateU/{id}")
    fun update(@Path("id") id: String, @Body userRequest: UserRequest): Call<UserResponse>

}