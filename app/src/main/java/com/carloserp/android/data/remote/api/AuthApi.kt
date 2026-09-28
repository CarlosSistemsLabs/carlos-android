package com.carloserp.android.data.remote.api

import com.carloserp.android.data.remote.dto.AuthResponseDto
import com.carloserp.android.data.remote.dto.LoginRequestDto
import com.carloserp.android.data.remote.dto.LogoutRequestDto
import com.carloserp.android.data.remote.dto.RefreshRequestDto
import com.carloserp.android.data.remote.dto.RefreshResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit definition of the backend auth endpoints (task 50.1).
 *
 * Paths are relative to the `/api/v1/` base URL configured in
 * [com.carloserp.android.data.remote.di.NetworkModule], so `"auth/login"`
 * resolves to `/api/v1/auth/login`. Calls are suspending and throw on non-2xx
 * responses; repositories wrap them with
 * [com.carloserp.android.core.network.safeApiCall] to get a typed result.
 */
interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponseDto

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshRequestDto): RefreshResponseDto

    @POST("auth/logout")
    suspend fun logout(@Body body: LogoutRequestDto)
}
