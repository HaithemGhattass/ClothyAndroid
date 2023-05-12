package com.clothy.clothyandroid
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CategoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(content = {
                Text(
                    "Start Matching",
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoryItem(
                title = "Outwear",
                image = painterResource(R.drawable.outwear),
                onClick = {navController.navigate("Tinder/outwear")}
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryItem(
                title = "Jeans",
                image = painterResource(R.drawable.jeans),
                onClick = {navController.navigate("Tinder/jeans")}

            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryItem(
                title = "Shoes",
                image = painterResource(R.drawable.shoes),
                onClick = {navController.navigate("Tinder/shoes")}

            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryItem(
                title = "Hat",
                image = painterResource(R.drawable.hat),
                onClick = {navController.navigate("Tinder/hat")}

            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryItem(
                title = "T-Shirt",
                image = painterResource(R.drawable.tshirt),
                onClick = {navController.navigate("Tinder/t-shirt")}

            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryItem(title: String, image: Painter,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .height(150.dp)
            .clip(shape = RoundedCornerShape(40.dp)),
        backgroundColor = Color(0xFFE4E4E4) ,
        onClick = onClick
    )
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 50.dp, end = 8.dp)
            )
            Image(
                painter = image,
                contentDescription = title,
                modifier = Modifier
                    .padding(start = 60.dp, top = 15.dp, end = 16.dp, bottom = 8.dp)
                    .width(120.dp)
                    .height(120.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
            )

        }
    }
    Divider(
        color = Color.LightGray.copy(alpha = 0.9f),
        thickness = 1.dp,
        modifier = Modifier.padding(top = 5.dp)
    )
}
