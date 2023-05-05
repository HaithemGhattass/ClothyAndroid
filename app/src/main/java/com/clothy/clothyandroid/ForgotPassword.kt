package com.clothy.clothyandroid

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clothy.clothyandroid.onBoarding.buildExoPlayer
import com.clothy.clothyandroid.onBoarding.buildPlayerView
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserRequest
import com.clothy.clothyandroid.services.UserResponse
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.WatchEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(videoUri: Uri,navController: NavController) {
    val exoPlayer = remember { MyApplication.getInstance().buildExoPlayer(videoUri) }
    Scaffold {
        DisposableEffect(
            AndroidView(
                factory = { it.buildPlayerView(exoPlayer) },
                modifier = Modifier.fillMaxSize()
            )
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
        val emailState = remember { mutableStateOf("") }
        val navigationcontroller = rememberNavController()
        val context = LocalContext.current
        val passwordState = remember { mutableStateOf("") }
        var showCodeField by remember { mutableStateOf(false) }
        var code = remember { mutableStateOf("") }
        var codeSent by remember { mutableStateOf(false) }

        var showPasswordFields by remember { mutableStateOf(true) }
        var password = remember { mutableStateOf("") }
        var confirmPassword = remember { mutableStateOf("") }



        Box {

            Column(modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 80.dp)
                .fillMaxSize()) {
                Text(
                    text = "Reset your\nPassword",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(0.dp)

                    ) {
if(showPasswordFields) {

        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, null) },
            label = { Text(text = "Email") },
            placeholder = { Text("Enter your email") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(0.5f),
                    shape = RoundedCornerShape(percent = 50)
                )
                .padding(horizontal = 1.dp),
            shape = RoundedCornerShape(percent = 50),
        )

    Spacer(modifier = Modifier.height(16.dp))

        if (showCodeField) {


            TextField(
                value = code.value,
                onValueChange = { code.value = it },
                placeholder = { Text("Enter your code") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(0.5f),
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .padding(horizontal = 1.dp),
                shape = RoundedCornerShape(percent = 50),
            )
        }



    Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
    Button(
        onClick = {
            if (codeSent) {
                // Confirm code
                // You can add your code verification logic here
                // and then reset the state variables
                showCodeField = false
                codeSent = false
                showPasswordFields =false
                confirmcode(emailState.value,code.value.toInt())
            } else {
                // Send code
                // You can add your code sending logic here
                // and then set the state variables
                sendcode(emailState.value.toString())
                codeSent = true
                showCodeField = true
            }

        },

        modifier = Modifier
            .border(
            width = 1.dp,
            color = Color.White.copy(0.5f),
            shape = RoundedCornerShape(percent = 50)
        ).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
       /* colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(151, 169, 246, alpha = 0x32),
            contentColor = Color.White
        )

        */
    ) {
        Text(
            if (codeSent)
                "Comfirm Code" else "Send Code",
            Modifier.padding(vertical = 8.dp)
        )
    }
} else {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = RoundedCornerShape(percent = 50)
            )
    ) {
        var passwordVisibility by remember { mutableStateOf(false) }

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = { Text("new password") },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black.copy(0.5f),
                    shape = RoundedCornerShape(percent = 50)
                )
                .padding(horizontal = 1.dp),
            shape = RoundedCornerShape(percent = 50),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                        tint = Color.White
                    )
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = RoundedCornerShape(percent = 50)
            )
    ) {

            var passwordVisibility by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                placeholder = { Text("confirm new password") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(0.5f),
                        shape = RoundedCornerShape(percent = 50)
                    )
                    .padding(horizontal = 1.dp),
                shape = RoundedCornerShape(percent = 50),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                            tint = Color.White
                        )
                    }
                }
            )

    }


    Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
    Button(
        onClick = {
             resetpwd(password.value)
            navController.navigate(NavigationItem.Login.route)
        },
        enabled = password.value == confirmPassword.value,

                shape = RoundedCornerShape(percent = 50),
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.White.copy(0.5f),
            shape = RoundedCornerShape(percent = 50)
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(151, 169, 246, alpha = 0x32),
            contentColor = Color.White
        )
    ) {
        Text(
            "Reset Password",
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
                        Spacer(Modifier.padding(bottom = 7.dp))

                        Text(
                            "Dont have an account? Sign up",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.White,
                            modifier = Modifier.clickable {


                            }

                        )
                    }




            }

        }
    }
}

fun sendcode(email: String){
    val request = UserRequest()
    request.email = email
    val retro = RetrofitClient().getInstance().create(ApiService::class.java)
    Log.e("firstname", email+"hahahahha")

    retro.forgetpass(request).enqueue(object : Callback<UserResponse> {

        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

            val user = response.body()
            Log.e("firstname", email)

            if (response.isSuccessful){
                Log.e("firstname", user!!.userr?.firstname!!)
            }

        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}
fun confirmcode(email: String, code: Int){
    val request = UserRequest()
    request.email = email
    request.code = code
    val retro = RetrofitClient().getInstance().create(ApiService::class.java)
    retro.confirmcode(request).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            val user = response.body()
            Log.e("firstname", code.toString())
            if (response.isSuccessful){
                Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("firstname", code.toString())
            }
        }
        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}
fun resetpwd(newPassword:String)
{
    val request = UserRequest()
    request.newPassword = newPassword

    val retro = RetrofitClient().getInstance().create(ApiService::class.java)
    retro.Resetpwd(request).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            val user = response.body()
            Log.e("firstname", newPassword.toString())
            if (response.isSuccessful){
                Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("firstname", newPassword)
            }
        }
        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}

