package com.clothy.clothyandroid.services

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    data class ForgotPasswordRequest(val email: String)

    data class ForgotPasswordResponse(val message: String)
    @POST("api/login")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @POST("api/register")
    fun register(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @GET("api/allUser")
    fun getUsers(): Call<List<UserResponse.User>>
    @PUT("api/updateU/{id}")
    fun update(@Path("id") id: String, @Body userRequest: UserRequest): Call<UserResponse>
    @POST("api/forgetpwd")
    fun forgetpass(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @POST("api/changepwcode")
    fun confirmcode(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
    @PUT("api/changepass")
    fun Resetpwd(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
}