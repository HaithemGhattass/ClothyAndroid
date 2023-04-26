package com.clothy.clothyandroid.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clothy.clothyandroid.categories
import com.clothy.clothyandroid.entities.TinderCard
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.clothy.clothyandroid.services.RetrofitClient
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CategoryList() {
    var outfits by remember { mutableStateOf(emptyList<OutfitResponse.Outfit>()) }
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val id = sharedPreferences.getString("id","")
    val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
    retro.getoutfit().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
        override fun onResponse(
            call: Call<List<OutfitResponse.Outfit>>,
            response: Response<List<OutfitResponse.Outfit>>
        ) {

            if (response.isSuccessful) {
                val user = response.body()

                //user?.get(0)?.type?.let { Log.e("type", it) }
                if (user != null) {
                   outfits = user
                }
            }
            else{
                Log.e("Error", "error")
            }
        }

        override fun onFailure(call: Call<List<OutfitResponse.Outfit>>, t: Throwable) {
            // Handle failure
        }
    })
LazyRow (
    modifier = Modifier.padding(vertical = 30.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
    items(outfits) {outfit ->
        CategoryCard(
           outfit
        )
    }
}
}

@Preview
@Composable
fun CategoryListPreview() {
    ClothyAndroidTheme {
        CategoryList()
    }
}