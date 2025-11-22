package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.WorkspaceRating
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorkspaceRatingRepository: JpaRepository<WorkspaceRating, UUID> {
    fun findAllByWorkspaceId(workspaceId: UUID):List<WorkspaceRating>
    fun findByWorkspaceIdAndUserId(workspaceId: UUID, userId: UUID): WorkspaceRating?
}