package com.newyork.sharespace.services.util

import org.springframework.data.domain.*

fun <T> paginate(list: List<T>, pageable: Pageable): Page<T> {
    val start = pageable.offset.toInt()
    val end = minOf(start + pageable.pageSize, list.size)
    val pageContent = if (start <= end) list.subList(start, end) else emptyList()
    return PageImpl(pageContent, pageable, list.size.toLong())
}

fun buildPageable(
    page: Int,
    size: Int,
    sortBy: String = "createdAt",
    direction: String = "desc"
): Pageable {
    val sort = if (direction.equals("desc", true))
        Sort.by(sortBy).descending()
    else
        Sort.by(sortBy).ascending()

    return PageRequest.of(page, size, sort)
}