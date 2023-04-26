package com.clothy.clothyandroid.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageResponse {

    @SerializedName("match")
    @Expose
    var MSG: Message? =null

    class Message {
        @SerializedName("_id")
        @Expose
        var Id: String? = null
        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("to")
        @Expose
        var to: String? = null

        @SerializedName("from")
        @Expose
        var from: String? =null

        @SerializedName("matchID")
        @Expose
        var matchID: Array<String>? =null
        var isSender: Boolean = false
    }
}