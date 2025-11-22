package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.WorkspaceReview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WorkspaceReviewRepository : JpaRepository<WorkspaceReview, UUID> {
    fun findAllByWorkspaceIdOrderByCreatedAtDesc(workspaceId: UUID, pageable: Pageable): Page<WorkspaceReview>
    fun findByWorkspaceIdAndUserId(workspaceId: UUID, userId: UUID): WorkspaceReview?
}