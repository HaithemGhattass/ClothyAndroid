package com.clothy.clothyandroid.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.clothy.clothyandroid.R
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme

@Composable
fun CategoryCard(outfit: OutfitResponse.Outfit) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(27.dp))
            .background(Color.LightGray)
            .border(
                width = 1.dp,
                color = Color.White.copy(0.5f),
                shape = RoundedCornerShape(27.dp)
            )
            .height(150.dp)
            .width(120.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(RetrofitClient().BASE_URLL + outfit.photo.toString()),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

                Box(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    // Show the lock icon if the outfit is locked
                    if (outfit.locked!!) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_lock_24),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }
            }
            Text(
                text = outfit.type.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }




@Preview
@Composable
fun PreviewCategoryCard() {
    ClothyAndroidTheme() {
        Column() {


        }
   }
}