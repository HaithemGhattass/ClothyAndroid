package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.adapters.SliderAdapter
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [account.newInstance] factory method to
 * create an instance of this fragment.
 */
class account : Fragment() {


    lateinit var sliderView: SliderView

    fun AccountFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
          val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
          val username = sharedPreferences?.getString("firstname", "")
          val email = sharedPreferences?.getString("email", "")
          val lastname = sharedPreferences?.getString("lastname", "")
          val fullname = username+" "+lastname
          val usernameTextView = view.findViewById<TextView>(R.id.name)
          val emailTextView = view.findViewById<TextView>(R.id.email)
        val image = view.findViewById<ImageView>(R.id.profile_image)
        val editButton = view.findViewById<LinearLayout>(R.id.editP)
        val url = "http://192.168.1.10:9090/uploads/IMAGE_1670987951352.webp"
        Glide.with(this).load(url).into(image)
        editButton.setOnClickListener{
            val intent = Intent(context, EditProfile::class.java)
            startActivity(intent)
        }
          val butt = view.findViewById<LinearLayout>(R.id.Logout)
        sliderView = view.findViewById(R.id.slider_view)


        val adapter = SliderAdapter(activity)

        sliderView.setSliderAdapter(adapter)




          usernameTextView.text = fullname
          emailTextView.text = email
/*
          butt.setOnClickListener{
              val editor = sharedPreferences?.edit()
              if (editor != null) {
                  editor.clear()
                  editor.apply()
              }
              val intent = Intent(context, LoginActivity::class.java)
              startActivity(intent)
          }

        */

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment account.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            account().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}