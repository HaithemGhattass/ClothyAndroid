package com.clothy.clothyandroid

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserRequest
import com.clothy.clothyandroid.services.UserResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates

class EditProfile : AppCompatActivity() {
    lateinit var birthdate: EditText
    lateinit var firstname: EditText
    lateinit var lastname: EditText
    lateinit var pseudo: EditText
    lateinit var phone :EditText
    lateinit var Done : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        firstname =findViewById(R.id.first)
        lastname = findViewById(R.id.last)
        pseudo = findViewById(R.id.pseudo)
        phone = findViewById(R.id.phone)
        val tele = phone.text.toString()
        birthdate = findViewById(R.id.birthdate)
        Done= findViewById(R.id.btnDone)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val F = sharedPreferences?.getString("firstname", "")
        val P = sharedPreferences?.getString("pseudo", "")
        val L = sharedPreferences?.getString("lastname", "")
        val PH = sharedPreferences?.getString("phone", "")
        firstname.setText(F.toString())
        pseudo.setText(P.toString())
        lastname.setText(L.toString())
        phone.setText(PH.toString())

        Done.setOnClickListener {
            update(firstname.text.toString(),lastname.text.toString(),pseudo.text.toString(),phone.text.toString().toInt(),birthdate.text.toString())
            finish()
        }

    }
    fun update(firstname:String,lastname:String,pseudo:String,phone:Int,birthdate:String)
    {
        val request = UserRequest()


        request.phone = phone
        request.pseudo= pseudo
        request.firstname= firstname
        request.lastname = lastname
        request.date= birthdate

        val retro = RetrofitClient().getInstance().create(ApiService::class.java)
        retro.update(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                val user = response.body()
                if (response.isSuccessful) {
                    Log.e("firstname", user!!.userr?.firstname!!)
                    Log.e("email", user!!.userr?.email!!)
                    Log.e("lastname", user!!.userr?.lastname!!)
                    Log.e("phone", user!!.userr?.phone!!.toString())
                    Log.e("gender", user!!.userr?.gender!!)
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    // Save the user's email address in shared preferences
                    editor.putString("email", user!!.userr?.email!!)
                    editor.putString("firstname", user!!.userr?.firstname!!)
                    editor.putString("lastname", user!!.userr?.lastname!!)
                    editor.putString("phone", user!!.userr?.phone!!.toString())
                    editor.putString("pseudo", user!!.userr?.pseudo!!)
                    // Commit the changes to the SharedPreferences object
                    editor.apply()
                }


            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("Error", it) }
            }
        })
    }

    fun goBack(view: View) {
        finish()
    }
}

