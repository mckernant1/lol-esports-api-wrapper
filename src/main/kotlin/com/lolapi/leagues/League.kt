package com.lolapi.leagues

data class League(
    val name: String,
    val slug: String,
    val id: String,
    val image: String,
    val priority: Int,
    val region: String
)

