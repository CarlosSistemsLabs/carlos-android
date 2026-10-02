package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.CategoryDto
import com.carloserp.android.data.remote.dto.CreateCategoryDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit definition of the categories endpoints (create products from the app).
 * `GET /categories` returns a flat array (no paging envelope). Tenant scoping
 * comes from the Bearer token.
 */
interface CategoryApi {

    @GET("categories")
    suspend fun list(): List<CategoryDto>

    @POST("categories")
    suspend fun create(@Body body: CreateCategoryDto): CategoryDto
}
