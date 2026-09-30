package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.CustomerDto
import com.carloserp.android.domain.model.Customer

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
