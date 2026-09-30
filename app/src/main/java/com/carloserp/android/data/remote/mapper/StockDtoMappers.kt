package com.carloserp.android.data.remote.mapper

import com.carloserp.android.data.remote.dto.StockLevelDto
import com.carloserp.android.domain.model.StockLevel

/** Maps the network [StockLevelDto] to the domain [StockLevel] (task 50.4). */
fun StockLevelDto.toDomain(): StockLevel =
    StockLevel(
        productId = productId,
        branchId = branchId,
        productName = productName,
        quantity = quantity,
        minStock = minStock,
        lowStock = lowStock,
    )
