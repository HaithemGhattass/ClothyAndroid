package com.clothy.clothyandroid.onBoarding

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clothy.clothyandroid.MyApplication
import com.clothy.clothyandroid.NavigationItem
import com.clothy.clothyandroid.R
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserRequest
import com.clothy.clothyandroid.services.UserResponse
import com.google.accompanist.insets.ProvideWindowInsets
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun onBoardingScreen(videoUri: Uri,
    navigateAction: () -> Unit , navController: NavController
) {
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
        Box {
            Column(modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 50.dp)
                .fillMaxSize()) {
                Text(
                    text = "Welcome to\nClothy",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(vertical = 0.dp)

                    ) {


                        TextField(
                            value = emailState.value,
                            onValueChange = { emailState.value = it },
                            leadingIcon = { Icon(imageVector = Icons.Filled.Person, null) },
                            label = { Text(text = "Email") },
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
                                .padding(horizontal = 0.dp),
                            shape = RoundedCornerShape(percent = 50)
                        )
                        // if true ( x

                        Spacer(modifier = Modifier.height(16.dp))

                            var passwordVisibility by remember { mutableStateOf(false) }

                            TextField(
                                value = passwordState.value,
                                onValueChange = { passwordState.value = it },
                                leadingIcon = { Icon(imageVector = Icons.Default.Lock, null) },
                                label = { Text(text = "Password") },
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
                                    .padding(horizontal = 0.dp),
                                shape = RoundedCornerShape(percent = 50),
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        passwordVisibility = !passwordVisibility
                                    }) {
                                        Icon(
                                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                            tint = Color.Black
                                        )
                                    }
                                }
                            )

                        Spacer(Modifier.padding(bottom = 7.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Forgot password?", color = Color.White)
                            TextButton(onClick = {navController.navigate(NavigationItem.forgitpwd.route)}) {
                                Text("CLICk HERE")
                            }
                        }

                        Spacer(Modifier.padding(bottom = 0.dp, top = 7.dp))
                        Button(onClick = {
                            login(
                                emailState.value,
                                passwordState.value,
                                navigateAction,
                                context
                            )
                        }, modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),

                            ) {
                            Text("SIGN IN", Modifier.padding(vertical = 8.dp))
                        }

                        Spacer(Modifier.padding(bottom = 0.dp))
                        Divider(
                            color = Color.White.copy(alpha = 0.9f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(top = 50.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Don't have an account?", color = Color.White)
                            TextButton(onClick = {navController.navigate(NavigationItem.Signup.route)}) {
                                Text("SING UP")
                            }
                        }
                    }

                }


            }

        }
    }





fun login(email: String, password: String, navigateAction: () -> Unit, context: Context)
{


    val request = UserRequest()
    request.email = email
    request.password =password
    val retro = RetrofitClient().getInstance().create(ApiService::class.java)
    retro.login(request).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

            val user = response.body()
            Log.e("msg",response.message())
            if (response.isSuccessful){
                navigateAction()
                if (user != null) {
                    Log.e("firstname", user.userr?.id!!)
                    val sessionId = user.userr?.id!! // Replace with actual session ID

                    val cookieManager = CookieManager()
                    CookieHandler.setDefault(cookieManager)
                    val cookie = HttpCookie("session", sessionId)
                    cookie.setPath("http://192.168.1.10:9090/api/login")
                    cookieManager.getCookieStore().add(null, cookie)
                }
                if (user != null) {
                    Log.e("email", user.userr?.email!!)
                }
                if (user != null) {
                    Log.e("id", user.userr?.id!!)
                }

                // Get the SharedPreferences object for your app

                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                // Get an instance of SharedPreferences.Editor
                val editor = sharedPreferences.edit()
                // Save the user's email address in shared preferences
                editor.putString("id", user!!.userr?.id!!)
                editor.putString("email", user!!.userr?.email!!)
                editor.putString("firstname", user!!.userr?.firstname!!)
                editor.putString("lastname", user!!.userr?.lastname!!)
                editor.putString("pseudo", user!!.userr?.pseudo!!)
                editor.putString("phone", user!!.userr?.phone!!.toString())
                editor.putString("gander", user!!.userr?.gender!!)
                editor.putString("image", user!!.userr?.image!!)
               // editor.putString("pseudo", user!!.userr?.pseudo!!)
                // Commit the changes to the SharedPreferences object
                editor.commit()
                /*TODO:call main activity*/

            }

        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}


