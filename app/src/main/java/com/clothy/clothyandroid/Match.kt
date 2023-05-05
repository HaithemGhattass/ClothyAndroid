package com.clothy.clothyandroid

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.adapters.MessageAdapter
import com.clothy.clothyandroid.entities.Like
import com.clothy.clothyandroid.entities.MessageItem
import com.clothy.clothyandroid.services.MatchResponse
import com.clothy.clothyandroid.services.MatchService
import com.clothy.clothyandroid.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
var matchs : List<MatchResponse.Match>? = null
object MessageItemHolder {
    var messageItemList: List<MessageItem>? = null
}
val TAG = MainActivity::class.java.simpleName
var messageList = ArrayList<MessageItem>()
var mAdapter: MessageAdapter? = null
val msg = mutableListOf<String>()
val c = mutableListOf<Int>()
val msn = mutableListOf<String>()
val idd = mutableListOf<String>()
val idR = mutableListOf<String>()
val ii = mutableListOf<String>()


@Composable
fun Match() {

    AndroidView(
    factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.activity_chat, null)
        val retro = RetrofitClient().getInstance().create(MatchService::class.java)
        retro.getmatch().enqueue(object : Callback<List<MatchResponse.Match>> {
            override fun onResponse(
                call: Call<List<MatchResponse.Match>>,
                response: Response<List<MatchResponse.Match>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                   // user?.get(0)?.Etat?.let { Log.e("type", it.toString()) }
                    if (user != null) {
                        idR.clear()
                        idd.clear()
                        msg.clear()
                        c.clear()
                        msn.clear()
                        ii.clear()
                        for (userr in user) {
                            println(userr.userr?.email.toString())
                            idd.add(userr.Id.toString())
                            msg.add(userr.userr?.firstname.toString())
                            c.add(user.size)
                            msn.add(userr.userr?.lastname.toString())
                            idR.add((userr.userr?.id.toString()))
                            ii.add(RetrofitClient().BASE_URLL+userr.userr?.image.toString())
                            println(userr.userr?.image.toString())
                            matchs = listOf(userr)
                            prepareMessageList()

                            mAdapter?.notifyDataSetChanged()

                        }
                        MessageItemHolder.messageItemList = messageList


                    }
                }

            }

            override fun onFailure(call: Call<List<MatchResponse.Match>>, t: Throwable) {
                // Handle failure
                Log.e("Error", "error")
            }
        })




        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_messages)
        messageList = ArrayList()
        mAdapter = MessageAdapter(messageList ?: ArrayList())
        Log.e("hahah", messageList.toString())

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.adapter = mAdapter
        val searchView = view.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val filter = mAdapter!!.filter
                filter.filter(s)
                Log.e("khdemt","el aasba")
                mAdapter?.notifyDataSetChanged()
            }
        })


        view
    },
    update = { view ->
        // Update the view here if necessary

    }


    )

}
private fun prepareMessageList() {
    val rand = Random()
    val id = rand.nextInt(100)
    var i: Int
    i = 0
    println(msg.size)
    while (i < msg.size) {
        val message = MessageItem(
            idd[i],
            idR[i],
            msg[i],
            msn[i],
            c[i],
            ii[i]
        )
        (messageList as ArrayList<MessageItem>).add(message)
        i++
        Log.e("hahah", messageList.toString())

    }
}