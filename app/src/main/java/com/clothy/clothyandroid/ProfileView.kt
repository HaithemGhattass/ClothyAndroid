package com.clothy.clothyandroid

import java.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor


@SuppressLint("MissingInflatedId")
@Composable
fun AccountScreen() {
    lateinit var photo: ImageView
     val cameraRequestId  = 1222

    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_accountt, null)
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences?.getString("firstname", "")
            val email = sharedPreferences?.getString("email", "")
            val lastname = sharedPreferences?.getString("lastname", "")
            val imagee = sharedPreferences?.getString("image","")
            val fullname = "$username $lastname"
            val usernameTextView = view.findViewById<TextView>(R.id.name)
            val emailTextView = view.findViewById<TextView>(R.id.email)
            val image = view.findViewById<ImageView>(R.id.profile_image)
            val editButton = view.findViewById<LinearLayout>(R.id.resetpassword)

editButton.setOnClickListener{
    val intent1 = Intent(context,ResetPassword::class.java)
    context.startActivity(intent1)


}
            photo=view.findViewById(R.id.photo)
            photo.setOnClickListener{
                // create a new AlertDialog.Builder
                // create a new AlertDialog.Builde

            }

            val url = RetrofitClient().BASE_URLL+imagee
            Glide.with(context).load(url).into(image)
            // Do any additional setup for the view here
            usernameTextView.text = fullname
            emailTextView.text = email
            view.findViewById<LinearLayout>(R.id.editP).setOnClickListener {
                // Handle the button click event here
                val intent = Intent(context, EditProfile::class.java)
                context.startActivity(intent)
            }
            view.findViewById<ImageView>(R.id.Logout).setOnClickListener{

                    val editor = sharedPreferences?.edit()
                    if (editor != null) {
                        editor.clear()
                        editor.apply()
                        RetrofitClient.CookieStorage.cookies.clear()

                        Log.e("cookies", RetrofitClient.CookieStorage.cookies.toString())
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
