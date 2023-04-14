package com.clothy.clothyandroid.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRequest {
    @SerializedName("email")
    @Expose
    var email:String? =null
    @SerializedName("password")
    @Expose
    var password:String? =null
    @SerializedName("firstname")
    @Expose
    var firstname:String? =null
    @SerializedName("lastname")
    @Expose
    var lastname:String? =null
    @SerializedName("phone")
    @Expose
    var phone:Int? =null
    @SerializedName("gender")
    @Expose
    var gender:String? =null
    @SerializedName("date")
    @Expose
    var date:String? =null
    @SerializedName("pseudo")
    @Expose
    var pseudo:String? =null

}