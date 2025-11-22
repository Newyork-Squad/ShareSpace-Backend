package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.AddReviewRequest
import com.newyork.sharespace.api.dto.AddWorkspaceRequest
import com.newyork.sharespace.api.dto.ReviewResponse
import com.newyork.sharespace.api.dto.workspace.SavedWorkspaceResponse
import com.newyork.sharespace.api.dto.workspace.WorkspaceResponse
import com.newyork.sharespace.api.dto.workspace.toWorkspaceResponse
import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
import com.newyork.sharespace.services.WorkspaceRatingService
import com.newyork.sharespace.services.WorkspaceReviewService
import com.newyork.sharespace.services.util.buildPageable
import com.newyork.sharespace.services.workspace.SavedWorkspaceService
import com.newyork.sharespace.services.workspace.WorkspaceService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/workspace")
@CrossOrigin(origins = ["*"])
class WorkspaceController(
    private val workspaceService: WorkspaceService,
    private val savedWorkspaceService: SavedWorkspaceService,
    private val ratingService: WorkspaceRatingService,
    private val reviewService: WorkspaceReviewService
) {

    @GetMapping("/all")
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "desc") direction: String
    ): ResponseEntity<ApiResponse<Page<WorkspaceResponse>>> {
        val result = workspaceService.getAllWorkspaces(buildPageable(page, size, sortBy, direction))
            .map { it.toWorkspaceResponse() }
        return ResponseEntity.ok(ApiResponse.success(result, "Workspaces retrieved successfully"))
    }

    @GetMapping("/category")
    fun getByCategory(
        @RequestParam category: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<Page<WorkspaceResponse>>> {
        val result = workspaceService.getByCategory(category, buildPageable(page, size))
            .map { it.toWorkspaceResponse() }
        return ResponseEntity.ok(ApiResponse.success(result, "Workspaces by category retrieved"))
    }

    @GetMapping("/near")
    fun getNearToYou(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<Page<WorkspaceResponse>>> {
        val result = workspaceService.getNearToYou(latitude, longitude, buildPageable(page, size))
            .map { it.toWorkspaceResponse() }
        return ResponseEntity.ok(ApiResponse.success(result, "Nearby workspaces retrieved"))
    }

    @GetMapping("/featured")
    fun getFeatured(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<Page<WorkspaceResponse>>> {
        val result = workspaceService.getFeaturedWorkspaces(buildPageable(page, size))
            .map { it.toWorkspaceResponse() }
        return ResponseEntity.ok(ApiResponse.success(result, "Featured workspaces retrieved"))
    }

    @PostMapping
    fun addWorkspace(
        @Valid @RequestBody workspaceRequest: AddWorkspaceRequest
    ): ResponseEntity<ApiResponse<WorkspaceResponse>> {
        val newWorkspace = workspaceService.addWorkspace(workspaceRequest)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(newWorkspace.toWorkspaceResponse(), "Workspace created successfully"))
    }

    @DeleteMapping("/{id}")
    fun deleteWorkspace(@PathVariable id: UUID): ResponseEntity<ApiResponse<String>> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("User not authenticated"))
        val workspace = workspaceService.getWorkspaceById(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Workspace with ID $id not found"))
        if (workspace.owner != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("You are not authorized to delete this workspace"))
        }
        workspaceService.deleteWorkspace(id)
        return ResponseEntity.ok(ApiResponse.success(message = "Workspace deleted successfully"))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<ApiResponse<WorkspaceResponse>> {
        val workspace = workspaceService.getWorkspaceById(id)
        return if (workspace != null) {
            ResponseEntity.ok(ApiResponse.success(workspace.toWorkspaceResponse(), "Workspace details retrieved"))
        } else {
            ResponseEntity.status(404).body(ApiResponse.error("Workspace not found"))
        }
    }

    @PostMapping("/saved/{workspaceId}")
    fun saveWorkspace(@PathVariable workspaceId: UUID): ResponseEntity<ApiResponse<SavedWorkspaceResponse>> {
        val userId = JwtAuthFilter.getUserId()
            ?: throw IllegalStateException("User not authenticated")
        val saved = savedWorkspaceService.saveWorkspace(userId, workspaceId)
        val response = SavedWorkspaceResponse(saved.id, saved.workspace.toWorkspaceResponse(), saved.createdAt)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Workspace saved successfully"))
    }

    @GetMapping("/saved")
    fun getSavedWorkspaces(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<Page<SavedWorkspaceResponse>>> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("User not authenticated"))

        val result = savedWorkspaceService.getSavedWorkspaces(userId, buildPageable(page, size))
        return ResponseEntity.ok(ApiResponse.success(result, "Saved workspaces retrieved successfully"))
    }


    @DeleteMapping("/saved/{workspaceId}")
    fun removeSavedWorkspace(@PathVariable workspaceId: UUID): ResponseEntity<ApiResponse<String>> {
        val userId = JwtAuthFilter.getUserId()
            ?: throw IllegalStateException("User not authenticated")
        savedWorkspaceService.removeSavedWorkspace(userId, workspaceId)
        return ResponseEntity.ok(ApiResponse.success(message = "Workspace removed from saved list"))
    }

    @PostMapping("/{workspaceId}/rate")
    fun rateWorkspace(@PathVariable workspaceId: UUID, @RequestParam rating: Int): ResponseEntity<ApiResponse<String>> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("User not authenticated"))
        ratingService.addOrUpdateRating(userId, workspaceId, rating)
        return ResponseEntity.ok(ApiResponse.success(message = "Rating submitted successfully"))
    }

    @PostMapping("/review")
    fun addReview(@RequestBody request: AddReviewRequest): ResponseEntity<ApiResponse<ReviewResponse>> {
        val userId = JwtAuthFilter.getUserId()
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("User not authenticated"))

        val existingReview = reviewService.getReviewByUserAndWorkspace(userId, request.workspaceId)
        if (existingReview != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("You have already submitted a review for this workspace"))
        }

        val review = reviewService.addReview(userId, request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(review, "Review added successfully"))
    }


    @GetMapping("/{workspaceId}/reviews")
    fun getReviews(
        @PathVariable workspaceId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ApiResponse<Page<ReviewResponse>>> {
        val pageable = buildPageable(page, size)
        val reviews = reviewService.getReviews(workspaceId, pageable)
        return ResponseEntity.ok(ApiResponse.success(reviews, "Reviews retrieved successfully"))
    }


    @GetMapping("/{workspaceId}/rating")
    fun getRating(@PathVariable workspaceId: UUID): ResponseEntity<ApiResponse<Map<String, Any>>> {
        val average = ratingService.getAverageRating(workspaceId)
        val count = ratingService.getRatingsCount(workspaceId)
        return ResponseEntity.ok(
            ApiResponse.success(
                mapOf("average" to average, "count" to count),
                "Rating retrieved successfully"
            )
        )
    }

}
