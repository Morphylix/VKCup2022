package com.morphylix.android.dzentest.domain.model

data class Category(
    val name: String,
    val imageResource: Int,
    var isChosen: Boolean = false
)
