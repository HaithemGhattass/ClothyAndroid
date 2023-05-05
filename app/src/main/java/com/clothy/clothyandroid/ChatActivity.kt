package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.adapters.MSG
import com.clothy.clothyandroid.adapters.MessageAdapter
import com.clothy.clothyandroid.adapters.msgAdapter
import com.clothy.clothyandroid.home.CategoryList
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
    private lateinit var send: Button
    private lateinit var username :TextView
    private lateinit var userImage : ImageView
    private lateinit var id :String
    private lateinit var sender :String
    private lateinit var idR :String
    private lateinit var imaageR :String
    private lateinit var reciver :String
    private lateinit var matchID :String
    private lateinit var idreciver :String




    private var ws: WebSocket? = null
    private var isListVisible = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatt)

        val shared = MyApplication.getInstance().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

         reciver = shared.getString("reciver","").toString()
         idreciver = shared.getString("idreciver","").toString()
         id = shared.getString("id","").toString()
         sender = shared.getString("firstname","").toString()
         idR = shared.getString("idreciver","").toString()
         imaageR = shared.getString("imageReciver","").toString()
         matchID = shared.getString("matchID","").toString()
        listView = findViewById(R.id.listView)
        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)
        username= findViewById(R.id.user_name)
        userImage= findViewById(R.id.user_image)
        val myListComposeView = findViewById<ComposeView>(R.id.myList)
        val toggleButton = findViewById<ImageButton>(R.id.toggleButton)

        myListComposeView.setContent {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                CategoryList("Swiped")
            }
        }
        toggleButton.setOnClickListener {
            isListVisible = !isListVisible
            myListComposeView.setContent {
                if (isListVisible) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        CategoryList("Swiped")
                    }
                }
            }
        }
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
                Rcieved.add(MSG("",text,"",sender!!,matchID,"","",true))

                editText.text.clear()
                val message = JSONObject()
                message.put("to", idreciver)
                message.put("idMatch", matchID)
                message.put("message", text)
                send(message.toString())
                listView.setSelection(adapterr.count - 1)
                adapterr.notifyDataSetChanged()
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

        val request: Request = Request.Builder().url("ws://10.0.2.2:9090/room/$matchID").build()
        val listener = EchoWebSocketListener(this::output, this::ping) { ws = null }
        val client = OkHttpClient.Builder()
            .addInterceptor(cookies.AddCookiesInterceptor(MyApplication.getInstance()))
            .addInterceptor(cookies.ReceivedCookiesInterceptor(MyApplication.getInstance()))
            .build()
        ws = client.newWebSocket(request, listener)
        username.text= reciver
        Glide.with(applicationContext)
            .load(imaageR)
            .into(userImage)
        listView.setSelection(listView.count - 1);
        button.visibility= View.GONE
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        adapterr.notifyDataSetChanged()

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

                if (messageItem.from == id)
                {
                    messageItem.isSender= true
                    messageItem.from= sender

                }
                if(messageItem.from == idR){
                    messageItem.isSender =false
                    messageItem.from= reciver
                }
                Rcieved.add(messageItem)
                Log.e("message",Rcieved[0].message)

                username.text= reciver
                Glide.with(applicationContext)
                    .load(imaageR)
                    .into(userImage)
                listView.setSelection(listView.count - 1);
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



