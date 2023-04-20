package com.clothy.clothyandroid

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.runtime.Composable
import com.bumptech.glide.Glide
import androidx.compose.material.Surface
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme


@Composable
fun AccountScreen() {
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_accountt, null)

            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences?.getString("firstname", "")
            val email = sharedPreferences?.getString("email", "")
            val lastname = sharedPreferences?.getString("lastname", "")
            val fullname = "$username $lastname"
            val usernameTextView = view.findViewById<TextView>(R.id.name)
            val emailTextView = view.findViewById<TextView>(R.id.email)
            val image = view.findViewById<ImageView>(R.id.profile_image)
            val editButton = view.findViewById<LinearLayout>(R.id.editP)
            val url = "http://192.168.1.10:9090/uploads/IMAGE_1670987951352.webp"
            Glide.with(context).load(url).into(image)
            // Do any additional setup for the view here
            usernameTextView.text = fullname
            emailTextView.text = email
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
@Composable
fun AccountScreenPreview() {
    ClothyAndroidTheme {
        Surface {
            AccountScreen()
        }
    }
}

@Preview
@Composable
fun AccountScreenPreviewLight() {
    AccountScreenPreview()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AccountScreenPreviewDark() {
    AccountScreenPreview()
}