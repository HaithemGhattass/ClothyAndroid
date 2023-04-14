package com.clothy.clothyandroid.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clothy.clothyandroid.services.WeatherData
import com.clothy.clothyandroid.services.WeatherViewModel
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
Scaffold(
    topBar = {
        TopAppBar(content = {
            Text(
                "Clothy Closet",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black

            )
        },
            backgroundColor = Color.Transparent,
            elevation = 0.dp
            )
    },
    backgroundColor = Color.White

) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "OutWear",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        CategoryList()
        Text(
            text = "Jeans",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        CategoryList()
        Text(
            text = "Hat",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        CategoryList()
        Text(
            text = "Shoes",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
        CategoryList()
        WeatherScreen("Tunis","15d5a0d5272b68569a261c345eb231a1",WeatherViewModel())
    }
}
}
@Preview
@Composable
fun PreviewHomeScreen() {
    ClothyAndroidTheme() {
        HomeScreen()
    }
}

@Composable
fun WeatherScreen(cityName: String, apiKey: String, viewModel: WeatherViewModel) {
    val weatherData = remember { mutableStateOf<WeatherData?>(null) }

    viewModel.getCurrentWeatherData(cityName, apiKey).observeForever {
        weatherData.value = it
    }

    if (weatherData.value != null) {
        // Display weather data
        Text(text = "Temperature: ${weatherData.value!!.temperature} C")
        Text(text = "Conditions: ${weatherData.value!!.description}")
       /* Image(
            painter = rememberImagePainter(data = "https://openweathermap.org/img/w/${weatherData.value!!.icon}.png"),
            contentDescription = "Weather icon"
        )*/
    } else {
        // Display loading indicator
        CircularProgressIndicator()
    }
}
