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

class EditProfile : AppCompatActivity() {
    lateinit var dateEdt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val register = findViewById<Button>(R.id.saveButton)

        dateEdt = findViewById(R.id.birthdate)
        dateEdt.setInputType(InputType.TYPE_NULL);
        dateEdt.setOnClickListener {
            val color = resources.getColor(R.color.purple, null)
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()
            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.setOnShowListener {
                val headerId = resources.getIdentifier("date_picker_header", "id", "android")
                val headerView = datePickerDialog.findViewById<View>(headerId)
                headerView?.setBackgroundColor(color)
            }

            datePickerDialog.show()
        }
        register.setOnClickListener{
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

            val id = sharedPreferences.getString("id", "")

            val firstname = findViewById<EditText>(R.id.editfirstName)
            val lastname = findViewById<EditText>(R.id.editlastname)
            val pseudo =findViewById<EditText>(R.id.editpseudoe)
            val pwd =findViewById<EditText>(R.id.editPassword)
            val phone =findViewById<EditText>(R.id.editphone)
            val i = (phone.text.toString()).replace("[\\D]".toRegex(), "").toInt()
            val birthdate = findViewById<EditText>(R.id.birthdate)

            if (id != null) {
                update(id,firstname.text.toString(),lastname.text.toString(),pseudo.text.toString(),pwd.text.toString(),i, birthdate.text.toString() )
            }


        }
    }
    fun update(id:String,firstname:String,lastname:String,pseudo:String,password:String,phone:Int,birthdate:String)
    {
        val request = UserRequest()

        request.password =password
        request.phone = phone
        request.pseudo= pseudo
        request.firstname= firstname
        request.lastname = lastname
        request.date= birthdate

        val retro = RetrofitClient().getInstance().create(ApiService::class.java)
        retro.update(id,request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                val user = response.body()
                Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("email", user!!.userr?.email!!)
                Log.e("lastname", user!!.userr?.lastname!!)
                Log.e("phone", user!!.userr?.phone!!.toString())
                Log.e("gender", user!!.userr?.gender!!)
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

                val editor = sharedPreferences.edit()
                // Save the user's email address in shared preferences
                editor.putString("id", user!!.userr?.id!!)
                editor.putString("email", user!!.userr?.email!!)
                editor.putString("firstname", user!!.userr?.firstname!!)
                editor.putString("lastname", user!!.userr?.lastname!!)
                editor.putString("phone", user!!.userr?.phone!!.toString())
                editor.putString("gander", user!!.userr?.gender!!)
                editor.putString("image", user!!.userr?.image!!)
                editor.putString("pseudo", user!!.userr?.pseudo!!)
                // Commit the changes to the SharedPreferences object
                editor.commit()


            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.e("Error", it) }
            }
        })
    }
    }

