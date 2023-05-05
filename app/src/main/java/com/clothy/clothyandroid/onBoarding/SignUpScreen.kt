package com.clothy.clothyandroid.onBoarding

import android.annotation.SuppressLint
import android.net.Uri
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.clothy.clothyandroid.R

import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue

import androidx.compose.runtime.remember
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.clothy.clothyandroid.MyApplication
import com.clothy.clothyandroid.MyComposable
import com.clothy.clothyandroid.NavigationItem
import com.clothy.clothyandroid.services.ApiService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.services.UserRequest
import com.clothy.clothyandroid.services.UserResponse
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(videoUri: Uri,

    navController: NavController

) {
    var radiostate = remember {
        mutableStateOf(0)
    }
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
        val firstnameState = remember { mutableStateOf("") }
        val lastnameState = remember { mutableStateOf("") }
        val pseudoState = remember { mutableStateOf("") }
        val cpasswordState = remember { mutableStateOf("") }
        val PhoneState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val pattern = remember { Regex("[a-zA-z\\s]*") }

        var pickedDate by remember {
            mutableStateOf(LocalDate.now())
        }

        val formattedDate by remember {
            derivedStateOf {
                DateTimeFormatter
                    .ofPattern("MMM dd yyyy")
                    .format(pickedDate)
            }
        }



        Box {

            Column(modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .fillMaxSize()) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
               Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(0.dp)

                    ) {
                        var showErrorFirstname by remember { mutableStateOf(false) }

                            TextField(
                                value  = firstnameState.value,
                                leadingIcon = { Icon(imageVector = Icons.Filled.Person, null) },
                                label = { Text(text = "Firstname") },
                                onValueChange = {
                                    if(it.matches(pattern)){
                                        firstnameState.value = it

                                    }
                                    showErrorFirstname = it== ""

                                },
                                placeholder = { Text("First name") },
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
                            shape = RoundedCornerShape(percent = 50)
                            )

                        if (showErrorFirstname) {
                            Text("Firstname is empty", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        var showErrorlastname by remember { mutableStateOf(false) }

                            TextField(
                                value  = lastnameState.value,
                                leadingIcon = { Icon(imageVector = Icons.Filled.Person, null) },
                                label = { Text(text = "Last Name") },
                                onValueChange = {
                                    if(it.matches(pattern)){
                                        lastnameState.value = it

                                    }
                                    showErrorlastname = it== ""

                                },
                                placeholder = { Text("Last name") },
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
                                shape = RoundedCornerShape(percent = 50)
                            )

                        if (showErrorlastname) {
                            Text("Lastname is empty", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }


                        Spacer(modifier = Modifier.height(16.dp))
                        var showErrorPseudo by remember { mutableStateOf(false) }


                            TextField(
                                value  = pseudoState.value,
                                leadingIcon = { Icon(imageVector = Icons.Filled.Person, null) },
                                label = { Text(text = "Pseudo") },
                                onValueChange = {pseudoState.value = it
                                    showErrorPseudo = it== ""

                                },
                                placeholder = { Text("Pseudo") },
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
                                shape = RoundedCornerShape(percent = 50)
                            )

                        if (showErrorPseudo) {
                            Text("Pseudo is empty", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        var showErrorMail by remember { mutableStateOf(false) }

                            TextField(
                                value  = emailState.value,
                                leadingIcon = { Icon(imageVector = Icons.Filled.Email, null) },
                                label = { Text(text = "Email") },
                                onValueChange = {
                                    emailState.value = it
                                    showErrorMail = !isValidEmail(it)

                                },
                                placeholder = { Text("Email") },
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
                                shape = RoundedCornerShape(percent = 50)
                            )


                        if (showErrorMail) {
                            Text("Email is invalid", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        var showPError by remember { mutableStateOf(false) }

                        Spacer(modifier = Modifier.height(16.dp))


                            var passwordVisibility by remember { mutableStateOf(false) }

                            TextField(
                                value = passwordState.value,
                                leadingIcon = { Icon(imageVector = Icons.Default.Lock, null) },
                                label = { Text(text = "Password") },
                                onValueChange = {passwordState.value = it
                                    showPError = passwordState.value.length < 6

                                },
                                placeholder = { Text("Password") },
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
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                        Icon(
                                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                            tint = Color.LightGray
                                        )
                                    }
                                }
                            )

                        if (showPError) {
                            Text("password length must be at least 6", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }

                        var showCPError by remember { mutableStateOf(false) }

                        Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
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
                                value = cpasswordState.value,
                                leadingIcon = { Icon(imageVector = Icons.Default.Lock, null) },
                                label = { Text(text = "Confirm Password") },
                                onValueChange = {cpasswordState.value = it
                                    showCPError = cpasswordState.value != passwordState.value

                                },
                                placeholder = { Text("Confirm Password") },
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
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                        Icon(
                                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                            contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                            tint = Color.LightGray
                                        )
                                    }
                                }
                            )
                        }
                        if (showCPError) {
                            Text("password does not match", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
                        var showPhoneError by remember { mutableStateOf(false) }

                            TextField(
                                value  = PhoneState.value,
                                leadingIcon = { Icon(imageVector = Icons.Default.PhoneAndroid, null) },
                                label = { Text(text = "Phone") },
                                onValueChange = {
                                    PhoneState.value = it
                                    showPhoneError = PhoneState.value.length != 8

                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        placeholder = { Text("Phone") },
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
                                shape = RoundedCornerShape(percent = 50)
                            )


                        if (showPhoneError) {
                            Text("Phone is invalid", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }

                        Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
                        var dateDialogState = rememberMaterialDialogState()

                            TextField(
                                enabled = false,
                                leadingIcon = { Icon(imageVector = Icons.Filled.EditCalendar, null) },
                                label = { Text(text = "Birthdate") },
                                value  = formattedDate,
                                onValueChange = {},
                                placeholder = { Text("Pick date") },
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
                                    .padding(horizontal = 1.dp)
                                    .clickable {
                                        dateDialogState.show()
                                    },
                            shape = RoundedCornerShape(percent = 50)
                            )
                            MaterialDialog(
                                dialogState = dateDialogState,
                                buttons = {
                                    positiveButton(text="OK") {
                                        /*Toast.makeText(
                                            applicationContext,
                                            "clicked ok",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        */
                                    }
                                    negativeButton(text="Cancel")
                                }
                            ) {
                                datepicker(
                                    initialDate = LocalDate.now(),
                                    title = "Pick a date",
                                    allowedDateValidator = {
                                       LocalDate.now().year - it.year in 0..79
                                        // LocalDate.now().year - it.year < 80
                                    }
                                ) {
                                    pickedDate = it
                                }
                            }

                        val radiooption = listOf("Male","Female","Other")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center



                        ) {
                            /*
                            Text(text = "Male", color = Color.White)
                            RadioButton(selected = radiostate.value, onClick = { radiostate.value = true })
                            Spacer(modifier = Modifier.width(50.dp))
                            Text(text = "Female",color = Color.White)
                            RadioButton(selected = !radiostate.value, onClick = { radiostate.value = false }, colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White
                            ))

                             */
                            radiooption.forEachIndexed { i,option: String ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,

                                ){
                                   RadioButton(selected = radiostate.value == i ,  colors = RadioButtonDefaults.colors(
                                selectedColor = Color.White),
                                       onClick = { radiostate.value = i })
                                    Spacer(modifier = Modifier.size(2.dp))
                                    Text(text = option, fontSize = 14.sp)
                            } }


                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (!isValidEmail(emailState.value) || emailState.value.isEmpty()  || firstnameState.value.isEmpty() || lastnameState.value.isEmpty() || pseudoState.value.isEmpty() || PhoneState.value.length != 8 || passwordState.value != cpasswordState.value || passwordState.value.length < 6 ){
                                    println("wrong")
                                }else{
                                    register(emailState.value,firstnameState.value,lastnameState.value,passwordState.value,PhoneState.value.replace("[\\D]".toRegex(),"").toInt(),radiooption[radiostate.value],pickedDate.toString(),navController)

                                }
                            //    Log.e("1",firstnameState.value + " " + lastnameState.value + " "+  emailState.value + " " + pseudoState.value + " " + passwordState.value + " "+ cpasswordState.value + " " + formattedDate + " " + radiooption[radiostate.value])
                                },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                        ) {
                            Text("SIGN IN", Modifier.padding(vertical = 8.dp))
                        }
                        Spacer(Modifier.padding(bottom = 18.dp))

                        Divider(
                            color = Color.White.copy(alpha = 0.9f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(top = 50.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Don't have an account?", color = Color.White)
                            TextButton(onClick = {navController.navigate(NavigationItem.Login.route)}) {
                                Text("SING IN")
                            }
                        }
                    }




            }

        }
    }
}


fun register(
    email: String,
    firstname: String,
    lastname: String,
    password: String,
    phone: Int,
    gender: String,
    birthdate: String,
    navController: NavController,
)
{
    val request = UserRequest()
    request.email = email
    request.password =password
    request.phone = phone
    request.gender = gender
    request.firstname= firstname
    request.lastname = lastname
    request.date= birthdate

    val retro = RetrofitClient().getInstance().create(ApiService::class.java)
    retro.register(request).enqueue(object : retrofit2.Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            if(response.isSuccessful){
                val user = response.body()
                Log.e("firstname", user!!.userr?.firstname!!)
                Log.e("email", user.userr?.email!!)
                Log.e("lastname", user.userr?.lastname!!)
                Log.e("phone", user.userr?.phone!!.toString())
                Log.e("gender", user.userr?.gender!!)
                navController.navigate(NavigationItem.Login.route)

            } else {
                Log.e("mataaditch","please check email")
            }



        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.e("Error", it) }
        }
    })
}

fun isValidEmail(emailStr: String?) =
    Pattern
        .compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
        ).matcher(emailStr).find()







