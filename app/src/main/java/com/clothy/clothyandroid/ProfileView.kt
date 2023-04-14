package com.clothy.clothyandroid

import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter



@Composable
fun AccountScreen() {
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_accountt, null)

            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences?.getString("firstname", "")
            val email = sharedPreferences?.getString("email", "")
            val lastname = sharedPreferences?.getString("lastname", "")
            val fullname = username+" "+lastname
            val usernameTextView = view.findViewById<TextView>(R.id.name)
            val emailTextView = view.findViewById<TextView>(R.id.email)
            val image = view.findViewById<ImageView>(R.id.profile_image)
            val editButton = view.findViewById<LinearLayout>(R.id.editP)
            val url = "http://172.16.3.224:9090:9090/uploads/IMAGE_1670987951352.webp"
            Glide.with(context).load(url).into(image)
            // Do any additional setup for the view here
            usernameTextView.setText(fullname)
            emailTextView.setText(email)
            view.findViewById<LinearLayout>(R.id.editP).setOnClickListener {
                // Handle the button click event here
                val intent = Intent(context, EditProfile::class.java)
                context.startActivity(intent)
            }
            view.findViewById<LinearLayout>(R.id.Logout).setOnClickListener{

                    val editor = sharedPreferences?.edit()
                    if (editor != null) {
                        editor.clear()
                        editor.apply()
                    }
                val intent2 = Intent(context, OnboardingActivity::class.java)
                context.startActivity(intent2)

            }

            view
        },
        update = { view ->
            // Update the view here if necessary

        }
    )
}
