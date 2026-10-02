package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.CreateCustomerDto
import com.carloserp.android.data.remote.dto.CustomerDto
import com.carloserp.android.domain.model.Customer
import com.carloserp.android.domain.model.NewCustomer

/** Maps the network [CustomerDto] to the domain [Customer] (task 50.4). */
fun CustomerDto.toDomain(): Customer =
    Customer(
        id = id,
        name = name,
        email = email,
        phone = phone,
        taxId = taxId,
        address = address,
        notes = notes,
        isActive = isActive,
    )

/** Maps a [NewCustomer] input to the create request body. */
fun NewCustomer.toDto(): CreateCustomerDto =
    CreateCustomerDto(
        name = name,
        email = email,
        phone = phone,
        taxId = taxId,
        address = address,
        notes = notes,
    )
