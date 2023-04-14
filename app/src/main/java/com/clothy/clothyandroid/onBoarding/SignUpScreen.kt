package com.clothy.clothyandroid.onBoarding

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
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
fun SignUpScreen(

    navController: NavController

) {
    var radiostate = remember {
        mutableStateOf(0)
    }


    Scaffold {
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
            Image(
                painter = painterResource(id = R.drawable.bg1),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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
                        var showErrorFirstname by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.5f),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                        ) {
                            TextField(
                                value  = firstnameState.value,
                                onValueChange = {
                                    if(it.matches(pattern)){
                                        firstnameState.value = it

                                    }
                                    showErrorFirstname = it== ""

                                },
                                placeholder = { Text("First name") },
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
                        if (showErrorFirstname) {
                            Text("Firstname is empty", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        var showErrorlastname by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.5f),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                        ) {
                            TextField(
                                value  = lastnameState.value,
                                onValueChange = {
                                    if(it.matches(pattern)){
                                        lastnameState.value = it

                                    }
                                    showErrorlastname = it== ""

                                },
                                placeholder = { Text("Last name") },
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
                        if (showErrorlastname) {
                            Text("Lastname is empty", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }


                        Spacer(modifier = Modifier.height(16.dp))
                        var showErrorPseudo by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.5f),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                        ) {
                            TextField(
                                value  = pseudoState.value,
                                onValueChange = {pseudoState.value = it
                                    showErrorPseudo = it== ""

                                },
                                placeholder = { Text("pseudo") },
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
                        if (showErrorPseudo) {
                            Text("Pseudo is empty", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        var showErrorMail by remember { mutableStateOf(false) }
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
                                onValueChange = {
                                    emailState.value = it
                                    showErrorMail = !isValidEmail(it)

                                },
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
                        if (showErrorMail) {
                            Text("Email is invalid", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        var showPError by remember { mutableStateOf(false) }

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
                                onValueChange = {passwordState.value = it
                                    showPError = passwordState.value.length < 6

                                },
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
                                onValueChange = {cpasswordState.value = it
                                    showCPError = cpasswordState.value != passwordState.value

                                },
                                placeholder = { Text("Comfirm Password") },
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
                        if (showCPError) {
                            Text("password does not match", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }
                        Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
                        var showPhoneError by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.5f),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                        ) {
                            TextField(
                                value  = PhoneState.value,
                                onValueChange = {
                                    PhoneState.value = it
                                    showPhoneError = PhoneState.value.length != 8

                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        placeholder = { Text("Phone") },
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
                        if (showPhoneError) {
                            Text("Phone is invalid", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                        }

                        Spacer(Modifier.padding(bottom = 7.dp, top = 7.dp))
                        var dateDialogState = rememberMaterialDialogState()
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(0.5f),
                                    shape = RoundedCornerShape(percent = 50)
                                )
                        ) {
                            TextField(
                                enabled = false,

                                value  = formattedDate,
                                onValueChange = {},
                                placeholder = { Text("Pick date") },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.Black,
                                    backgroundColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .clickable {
                                        dateDialogState.show()
                                    }
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
                            shape = RoundedCornerShape(percent = 50),
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.White.copy(0.5f),
                                shape = RoundedCornerShape(percent = 50)
                            )
                            ,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(151, 169, 246, alpha = 0x32), contentColor = Color.White)
                        ) {
                            Text(
                                "Sign Up",
                                modifier = Modifier.padding(horizontal = 40.dp, vertical = 4.dp)
                                ,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                        }
                        Spacer(Modifier.padding(bottom = 18.dp))

                        Text(
                            " have an account? Sign in",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.White,

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    navController.navigate(NavigationItem.Login.route)
                                }

                        )
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







