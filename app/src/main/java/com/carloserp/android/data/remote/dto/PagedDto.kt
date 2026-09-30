package com.carloserp.android.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Offset-paginated list envelope used by most backend list endpoints (task 50.4):
 * `{ "items": [...], "meta": { total, page, pageSize, totalPages } }`.
 *
 * Generic over the item DTO so every feature reuses it, e.g. `PagedDto<ProductDto>`.
 */
@Serializable
data class PagedDto<T>(
    val items: List<T> = emptyList(),
    val meta: PageMetaDto = PageMetaDto(),
)

@Serializable
data class PageMetaDto(
    val total: Int = 0,
    val page: Int = 1,
    val pageSize: Int = DEFAULT_PAGE_SIZE,
    val totalPages: Int = 0,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
