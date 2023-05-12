package com.clothy.clothyandroid

import java.util.UUID

data class Categoryy (
    val title: String,
    val image: Int,
    val id: UUID = UUID.randomUUID()
)

val categories = listOf<Categoryy>(
    Categoryy("jeans",R.drawable.card_music),
    Categoryy("outwear",R.drawable.card_art),
    Categoryy("t-shirt",R.drawable.card_vw),


    )