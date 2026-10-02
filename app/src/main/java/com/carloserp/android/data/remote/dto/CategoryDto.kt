package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/** Category as returned by `GET /categories` (only the fields the app needs). */
@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
)

/** Body of `POST /categories`. */
@Serializable
data class CreateCategoryDto(
    val name: String,
)
