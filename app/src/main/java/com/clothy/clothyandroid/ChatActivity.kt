package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.clothy.clothyandroid.adapters.MSG
import com.clothy.clothyandroid.adapters.MessageAdapter
import com.clothy.clothyandroid.adapters.msgAdapter
import com.clothy.clothyandroid.services.EchoWebSocketListener
import com.clothy.clothyandroid.services.EchoWebSocketListener.Companion.NORMAL_CLOSURE_STATUS
import com.clothy.clothyandroid.services.cookies
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONArray
import org.json.JSONObject

class ChatActivity : AppCompatActivity() , MessageAdapter.OnChatRoomClickListener{
    private val message : ImageButton by lazy { findViewById(R.id.button) }
    private val output: TextView by lazy { findViewById(R.id.messageTextView) }
    private val entryText: EditText by lazy { findViewById(R.id.editText) }
    private val client by lazy {
        OkHttpClient()
    }
    private lateinit var adapterr: msgAdapter
    private lateinit var recyclerView: RecyclerView
    private var Rcieved =  mutableListOf<MSG>()
    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private lateinit var button: ImageButton
    private lateinit var username :TextView
    private lateinit var userImage : ImageView
    private var ws: WebSocket? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatt)



        listView = findViewById(R.id.listView)
        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)
        username= findViewById(R.id.user_name)
        userImage= findViewById(R.id.user_image)

// Set the adapter for the recyclerView


         adapterr = msgAdapter(this, Rcieved)
        listView.adapter = adapterr

        listView.setSelection(listView.count - 1);
        adapterr.notifyDataSetChanged()
        Log.e("messages",Rcieved.size.toString())
        button.setOnClickListener {
            val message = editText.text.toString()
            Rcieved += MSG("Clark Kent", message, "true","","","","",true)
            adapterr.notifyDataSetChanged()
            editText.text.clear()
        }
        message.setOnClickListener {
            ws?.apply {
                val text = entryText.text.toString()
                val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val id = shared.getString("id","")
                val sender = shared.getString("firstname","")
                Rcieved.add(MSG("",text,"",sender!!,"64482ab9ae145a02d69d5d41","","",true))


                val reciver = shared.getString("reciver","")
                val idreciver = shared.getString("idreciver","")
                val message = JSONObject()
                message.put("to", "$idreciver")
                message.put("idMatch", "64482ab9ae145a02d69d5d41")
                message.put("message", "$text")
                send(message.toString())
                entryText.text.clear()
            } ?: ping("Error: Restart the App to reconnect")
        }
    }

    override fun onResume() {
        super.onResume()
     start()
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    private fun start() {
        val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
val matchID = shared.getString("matchID","")
        val request: Request = Request.Builder().url("ws://10.0.2.2:9090/room/$matchID").build()
        val listener = EchoWebSocketListener(this::output, this::ping) { ws = null }
        val client = OkHttpClient.Builder()
            .addInterceptor(cookies.AddCookiesInterceptor(MyApplication.getInstance()))
            .addInterceptor(cookies.ReceivedCookiesInterceptor(MyApplication.getInstance()))
            .build()
        ws = client.newWebSocket(request, listener)

    }

    private fun stop() {
        ws?.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
    }

    override fun onDestroy() {
        super.onDestroy()
        client.dispatcher.executorService.shutdown()
    }

    private fun output(txt: String) {
        runOnUiThread {

            val jsonString = """[$txt]"""

            val jsonArray = JSONArray(jsonString)
            val messageList = mutableListOf<MSG>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val messageItem = Gson().fromJson(jsonObject.getString("msg"), MSG::class.java)
                val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val id = shared.getString("id","")
                val sender = shared.getString("firstname","")
                val reciver = shared.getString("reciver","")
                if (messageItem.from == id)
                {
                    messageItem.isSender= true
                    messageItem.from=sender!!

                } else{
                    messageItem.isSender =false
                    messageItem.from=reciver!!
                }
                Rcieved.add(messageItem)
                Log.e("message",Rcieved[0].message)
                listView.setSelection(listView.count - 1);
                username.text= reciver
              adapterr.notifyDataSetChanged()
            }

            //"${output.text}\n${txt}".also { output.text = it }
        }
    }

    private fun ping(txt: String) {
        runOnUiThread {
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onChatRoomClick(roomId: String) {
        val request: Request = Request.Builder().url("ws://10.0.2.2:9090/room/$roomId").build()
        val listener = EchoWebSocketListener(this::output, this::ping) { ws = null }
        val client = OkHttpClient.Builder()
            .addInterceptor(cookies.AddCookiesInterceptor(MyApplication.getInstance()))
            .addInterceptor(cookies.ReceivedCookiesInterceptor(MyApplication.getInstance()))
            .build()
        ws = client.newWebSocket(request, listener)
    }
}



