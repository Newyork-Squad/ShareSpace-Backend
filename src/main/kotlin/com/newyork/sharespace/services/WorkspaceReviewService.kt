package com.newyork.sharespace.services

import com.newyork.sharespace.api.dto.AddReviewRequest
import com.newyork.sharespace.api.dto.ReviewResponse
import com.newyork.sharespace.repository.WorkspaceReviewRepository
import com.newyork.sharespace.services.entity.WorkspaceReview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkspaceReviewService(private val reviewRepository: WorkspaceReviewRepository) {

    fun addReview(userId: UUID, request: AddReviewRequest): ReviewResponse {
        val review = WorkspaceReview(
            workspaceId = request.workspaceId,
            userId = userId,
            rating = request.rating,
            comment = request.comment
        )
        val saved = reviewRepository.save(review)
        return ReviewResponse(
            id = saved.id,
            userId = saved.userId,
            rating = saved.rating,
            comment = saved.comment,
            createdAt = saved.createdAt
        )
    }

    fun getReviews(workspaceId: UUID, pageable: Pageable): Page<ReviewResponse> {
        return reviewRepository.findAllByWorkspaceIdOrderByCreatedAtDesc(workspaceId, pageable)
            .map { ReviewResponse(it.id, it.userId, it.rating, it.comment, it.createdAt) }
    }
}