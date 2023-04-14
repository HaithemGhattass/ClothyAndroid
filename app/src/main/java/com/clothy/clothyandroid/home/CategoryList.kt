package com.clothy.clothyandroid.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clothy.clothyandroid.categories
import com.clothy.clothyandroid.ui.theme.ClothyAndroidTheme

@Composable
fun CategoryList() {
LazyRow (
    modifier = Modifier.padding(vertical = 30.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
    items(categories) {category ->
        CategoryCard(
            title = category.title,
            image = painterResource(
                id = category.image
            )
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