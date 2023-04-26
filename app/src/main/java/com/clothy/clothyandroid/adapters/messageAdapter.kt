package com.clothy.clothyandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.clothy.clothyandroid.R

data class ChatMessage(val sender: String, val message: String, val isSender: Boolean)
data class MSG(val _id: String, val message: String, var to: String, var from: String, val matchID :String, val createdAt: String, val updatedAt:String,
               var isSender: Boolean)

class msgAdapter(context: Context, messages: MutableList<MSG>) :
    ArrayAdapter<MSG>(context, 0, messages) {

    companion object {
        private const val VIEW_TYPE_SENDER = 0
        private const val VIEW_TYPE_RECIPIENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message?.isSender == true) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_RECIPIENT
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)
        val viewType = getItemViewType(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            if (viewType == VIEW_TYPE_SENDER) {
                R.layout.messagesrow
            } else {
                R.layout.messagerowreciver
            }, parent, false
        )

        val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
        val senderTextView = view.findViewById<TextView>(R.id.senderTextView)

        messageTextView.text = message?.message
        senderTextView.text = message?.from

        return view
    }

}