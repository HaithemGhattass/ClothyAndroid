package com.clothy.clothyandroid.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.ChatActivity
import com.clothy.clothyandroid.MyApplication
import com.clothy.clothyandroid.R
import com.clothy.clothyandroid.entities.MessageItem
import com.clothy.clothyandroid.services.RetrofitClient
import okhttp3.WebSocket
import java.util.*
import kotlin.collections.ArrayList


class MessageAdapter(private val messageList: List<MessageItem>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(),Filterable {
    private var filteredList: List<MessageItem> = messageList
    private var ws: WebSocket? = null
    private var onChatRoomClickListener: OnChatRoomClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_message_item, parent, false)
        return MessageViewHolder(view)
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messageList[position]
        val context = holder.itemView.context
        holder.contentTextView.text = item.content
        holder.nameTextView.text = item.name

        holder.chat.setOnClickListener {
            val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = shared.edit()
            editor.putString("reciver", item.name)
            editor.putString("idreciver", item.idR)
            editor.putString("matchID",item.id)
            editor.putString("imageReciver",item.picture)
            editor.apply()
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("username", item.name+" "+item.content)
            intent.putExtra("matcher", item.idR)
            context.startActivity(intent)
            onChatRoomClickListener?.onChatRoomClick(item.id)
        }

        if (item.count <= 0) {
            holder.viewIndicator.visibility = View.INVISIBLE
        }

            Glide.with(context)
                .load(item.picture)
                .into(holder.thumbnail)

        // You can bind other views here
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        val contentTextView: TextView = itemView.findViewById(R.id.text_content)
       val  thumbnail : ImageView= itemView.findViewById(R.id.thumbnail)
        val viewIndicator:RelativeLayout =itemView.findViewById(R.id.layout_dot_indicator)
        val chat : RelativeLayout = itemView.findViewById(R.id.layout_message_content)
        // You can find other views here
    }
     override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = ArrayList<MessageItem>()
                if (constraint == null || constraint.isEmpty()) {
                    Log.d("FILTER_DEBUG", "getFilter called with constraint: $constraint")
                    Log.d("FILTER_DEBUG", "Filtered results: $filteredResults")
                    filteredResults.addAll(messageList)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    Log.d("FILTER_DEBUG", "getFilter called with constraint: $constraint")
                    Log.d("FILTER_DEBUG", "Filtered results: $filteredResults")
                    for (item in messageList) {
                        if (item.name.lowercase(Locale.ROOT).contains(filterPattern)) {
                            filteredResults.add(item)
                            Log.d("FILTER_DEBUG", "getFilter called with constraint: $constraint")
                            Log.d("FILTER_DEBUG", "Filtered results: $filteredResults")
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<MessageItem>
                notifyDataSetChanged()
            }
        }
    }

    interface OnChatRoomClickListener {
        fun onChatRoomClick(roomId: String)
    }

}
