package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.AuthResponseDto
import com.carloserp.android.data.remote.dto.UserDto
import com.carloserp.android.domain.model.AuthSession
import com.carloserp.android.domain.model.AuthUser

/**
 * Maps auth network DTOs to domain models (task 50.1), keeping the transport
 * shape out of the domain/presentation layers.
 */
fun AuthResponseDto.toDomain(): AuthSession =
    AuthSession(
        user = user.toDomain(),
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun UserDto.toDomain(): AuthUser =
    AuthUser(
        id = id,
        tenantId = tenantId,
        email = email,
        firstName = firstName,
        lastName = lastName,
        roleId = roleId,
    )
