package com.github.mckernant1.lol.heimerdinger.leagues

import kotlinx.serialization.Serializable

@Serializable
data class League(
    val name: String,
    val slug: String,
    val id: String,
    val image: String,
    val priority: Int,
    val region: String
)

