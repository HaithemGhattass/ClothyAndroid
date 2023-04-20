package com.clothy.clothyandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.adapters.SliderAdapter
import com.smarteist.autoimageslider.SliderView

class Accountt : AppCompatActivity()
{
    lateinit var sliderView: SliderView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountt)


        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("firstname", "")
        val email = sharedPreferences?.getString("email", "")
        val lastname = sharedPreferences?.getString("lastname", "")
        val fullname = username+" "+lastname
        val usernameTextView = findViewById<TextView>(R.id.name)
        val emailTextView = findViewById<TextView>(R.id.email)
        val image = findViewById<ImageView>(R.id.profile_image)
        val editButton = findViewById<LinearLayout>(R.id.editP)
        val url = "http://192.168.1.10:9090/uploads/IMAGE_1670987951352.webp"
        Glide.with(this).load(url).into(image)
        editButton.setOnClickListener{
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }
        val butt = findViewById<LinearLayout>(R.id.Logout)
        sliderView = findViewById(R.id.slider_view)


        val adapter = SliderAdapter(this)

        sliderView.setSliderAdapter(adapter)




        usernameTextView.text = fullname
        emailTextView.text = email
    }
}