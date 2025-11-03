package com.newyork.sharespace.api.controller

import com.newyork.sharespace.services.entity.Workspace
import com.newyork.sharespace.services.util.buildPageable
import com.newyork.sharespace.services.workspace.WorkspaceService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/workspace")
@CrossOrigin(origins = ["*"])
class WorkspaceController(private val workspaceService: WorkspaceService) {

    @GetMapping("/all")
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "desc") direction: String
    ): ResponseEntity<Page<Workspace>> =
        ResponseEntity.ok(
            workspaceService.getAllWorkspaces(
                buildPageable(page, size, sortBy, direction)
            )
        )


    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Workspace> =
        workspaceService.getWorkspaceById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()


    @GetMapping("/category")
    fun getByCategory(
        @RequestParam category: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<Workspace>> =
        ResponseEntity.ok(
            workspaceService.getByCategory(
                category,
                buildPageable(page, size)
            )
        )


    @GetMapping("/near")
    fun getNearToYou(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<Workspace>> =
        ResponseEntity.ok(
            workspaceService.getNearToYou(
                latitude, longitude,
                buildPageable(page, size)
            )
        )


    @GetMapping("/featured")
    fun getFeatured(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<Workspace>> =
        ResponseEntity.ok(
            workspaceService.getFeaturedWorkspaces(
                buildPageable(page, size)
            )
        )
}