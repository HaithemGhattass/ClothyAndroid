package com.clothy.clothyandroid.onBoarding

import android.annotation.SuppressLint
import android.content.Context

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.clothy.clothyandroid.NavigationItem
import com.clothy.clothyandroid.R
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserRequest
import com.clothy.clothyandroid.services.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun onBoardingScreen(
    navigateAction: () -> Unit , navController: NavController
) {

    Scaffold {
        val emailState = remember { mutableStateOf("") }
        val navigationcontroller = rememberNavController()
        val context = LocalContext.current
        val passwordState = remember { mutableStateOf("") }
        Box {
            Image(
                painter = painterResource(id = R.drawable.bg1),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 80.dp)
                .fillMaxSize()) {
                Text(
                    text = "Welcome to\nClothy",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                Card(
                    elevation = 4.dp,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(0.1f),
                            shape = RoundedCornerShape(27.dp)
                        )
                        .clip(RoundedCornerShape(27.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cardblur),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(27.dp)

                    ) {

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.5f),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                        ) {
                            TextField(
                                value  = emailState.value,
                                onValueChange = {emailState.value = it},
                                placeholder = { Text("Email") },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
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

                            TextField(
                                value = passwordState.value,
                                onValueChange = {passwordState.value = it},
                                placeholder = { Text("Password") },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
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
                                login(
                                    emailState.value,
                                    passwordState.value,
                                    navigateAction,
                                    context
                                )
                            } ,
                            shape = RoundedCornerShape(percent = 50),
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.White.copy(0.5f),
                                shape = RoundedCornerShape(percent = 50)
                            ),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(151, 169, 246, alpha = 0x32), contentColor = Color.White)
                        ) {
                            Text(
                                "Login",
                                modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                        Spacer(Modifier.padding(bottom = 7.dp))

                        Text(
                            "Dont have an account? Sign up",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.White,
                            modifier = Modifier.clickable {
                            navController.navigate(NavigationItem.Signup.route)


                            }

                        )
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
               Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("email", user!!.userr?.email!!)
                Log.e("id", user!!.userr?.id!!)

                // Get the SharedPreferences object for your app

                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                // Get an instance of SharedPreferences.Editor
                val editor = sharedPreferences.edit()
                // Save the user's email address in shared preferences
                editor.putString("id", user!!.userr?.id!!)
                editor.putString("email", user!!.userr?.email!!)
                editor.putString("firstname", user!!.userr?.firstname!!)
                editor.putString("lastname", user!!.userr?.lastname!!)
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


