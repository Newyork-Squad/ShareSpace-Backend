package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.Workspace
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorkspaceRepository : JpaRepository<Workspace, UUID> {

    fun findAllByOrderByRatingDesc(pageable: Pageable): Page<Workspace>

    fun findAllByOrderByPriceAsc(pageable: Pageable): Page<Workspace>

}