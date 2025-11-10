package com.newyork.sharespace.api.controller

import com.newyork.sharespace.api.dto.AddWorkspaceRequest
import com.newyork.sharespace.api.dto.workspace.SavedWorkspaceResponse
import com.newyork.sharespace.api.dto.workspace.WorkspaceResponse
import com.newyork.sharespace.api.dto.workspace.toWorkspaceResponse
import com.newyork.sharespace.config.JwtAuthFilter
import com.newyork.sharespace.config.exceptionHandling.ApiResponse
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
    private val savedWorkspaceService: SavedWorkspaceService
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


}
