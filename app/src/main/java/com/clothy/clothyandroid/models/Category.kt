package com.clothy.clothyandroid

import java.util.UUID

data class Category (
    val title: String,
    val image: Int,
    val id: UUID = UUID.randomUUID()
)

val categories = listOf<Category>(
    Category("jeans",R.drawable.card_music),
    Category("outwear",R.drawable.card_art),
    Category("t-shirt",R.drawable.card_vw),


    )