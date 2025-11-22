package com.newyork.sharespace.services

import com.newyork.sharespace.repository.WorkspaceRatingRepository
import com.newyork.sharespace.services.entity.WorkspaceRating
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class WorkspaceRatingService(private val ratingRepository: WorkspaceRatingRepository) {

    fun addOrUpdateRating(userId: UUID, workspaceId: UUID, ratingValue: Int): WorkspaceRating {
        val existing = ratingRepository.findByWorkspaceIdAndUserId(workspaceId, userId)
        val rating = existing?.copy(rating = ratingValue)
            ?: WorkspaceRating(workspaceId = workspaceId, userId = userId, rating = ratingValue)
        return ratingRepository.save(rating)
    }

    fun getAverageRating(workspaceId: UUID): Double {
        val ratings = ratingRepository.findAllByWorkspaceId(workspaceId)
        return if (ratings.isEmpty()) 0.0 else ratings.map { it.rating }.average()
    }

    fun getRatingsCount(workspaceId: UUID): Int =
        ratingRepository.findAllByWorkspaceId(workspaceId).size
}