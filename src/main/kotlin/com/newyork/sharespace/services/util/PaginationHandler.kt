package com.newyork.sharespace.services.util

import com.newyork.sharespace.services.entity.Workspace
import org.springframework.data.domain.*

fun paginate(list: List<Workspace>, pageable: Pageable): Page<Workspace> {
    val start = pageable.offset.toInt()
    val end = (start + pageable.pageSize).coerceAtMost(list.size)
    val content = if (start <= end) list.subList(start, end) else emptyList()
    return PageImpl(content, pageable, list.size.toLong())
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