package com.clothy.clothyandroid.services

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {


    fun getInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(/*"https://cicero-crm.com/api/"*/ "http://10.0.2.2:9090/")

            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}