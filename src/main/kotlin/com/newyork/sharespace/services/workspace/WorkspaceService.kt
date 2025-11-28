package com.newyork.sharespace.services.workspace

import com.newyork.sharespace.api.dto.AddWorkspaceRequest
import com.newyork.sharespace.api.dto.workspace.WorkspaceSearchRequest
import com.newyork.sharespace.api.dto.workspace.toWorkspaceEntity
import com.newyork.sharespace.repository.WorkspaceRepository
import com.newyork.sharespace.services.entity.Workspace
import com.newyork.sharespace.services.util.calculateDistanceInKm
import com.newyork.sharespace.services.util.paginate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkspaceService(private val workspaceRepository: WorkspaceRepository) {

    fun getAllWorkspaces(pageable: Pageable): Page<Workspace> =
        workspaceRepository.findAll(pageable)

    fun getWorkspaceById(id: UUID): Workspace? = workspaceRepository.findById(id).orElse(null)

    fun getByCategory(category: String, pageable: Pageable): Page<Workspace> =
        when (category.lowercase()) {
            Popular, TopRated -> workspaceRepository.findAllByOrderByRatingDesc(pageable)
            LowestPrice -> workspaceRepository.findAllByOrderByPriceAsc(pageable)
            else -> workspaceRepository.findAll(pageable)
        }

    fun getFeaturedWorkspaces(pageable: Pageable): Page<Workspace> {
        val featured = workspaceRepository.findAll()
            .sortedByDescending { it.amenities.size }

        return paginate(featured, pageable)
    }

    fun getNearToYou(latitude: Double, longitude: Double, pageable: Pageable): Page<Workspace> {
        val sortedByDistance = workspaceRepository.findAll()
            .sortedBy { calculateDistanceInKm(latitude, longitude, it.latitude, it.longitude) }

        return paginate(sortedByDistance, pageable)
    }

    fun addWorkspace(request: AddWorkspaceRequest): Workspace {
        val workspaceEntity = request.toWorkspaceEntity()
        return workspaceRepository.save(workspaceEntity)
    }

    fun deleteWorkspace(id: UUID) {
        if (!workspaceRepository.existsById(id)) {
            throw NoSuchElementException("Workspace with ID $id not found")
        }
        workspaceRepository.deleteById(id)
    }

    fun searchWorkspaces(request: WorkspaceSearchRequest, pageable: Pageable): Page<Workspace> {
        val allWorkspaces = workspaceRepository.findAll().asSequence()
            .filterByKeyword(request.keyword)
            .filterByPrice(request.minPrice, request.maxPrice)
            .filterByRating(request.minRating)
            .filterByServices(request.services?.toList())
            .toList()

        return paginate(allWorkspaces, pageable)
    }


    private fun Sequence<Workspace>.filterByKeyword(keyword: String?): Sequence<Workspace> =
        if (keyword.isNullOrBlank()) this
        else this.filter { workspace ->
            workspace.title.contains(keyword, ignoreCase = true) ||
                    workspace.description.contains(keyword, ignoreCase = true) ||
                    workspace.location.contains(keyword, ignoreCase = true)
        }

    private fun Sequence<Workspace>.filterByPrice(minPrice: Double?, maxPrice: Double?): Sequence<Workspace> =
        this.filter { workspace ->
            (minPrice?.let { workspace.price >= it } ?: true) &&
                    (maxPrice?.let { workspace.price <= it } ?: true)
        }

    private fun Sequence<Workspace>.filterByRating(minRating: Double?): Sequence<Workspace> =
        if (minRating == null) this
        else this.filter { (it.rating ?: 0.0) >= minRating }

    private fun Sequence<Workspace>.filterByServices(services: List<String>?): Sequence<Workspace> =
        if (services.isNullOrEmpty()) this
        else this.filter { workspace ->
            workspace.amenities.map { it.name }.containsAll(services)
        }


    private companion object {
        const val Popular = "popular"
        const val TopRated = "top_rated"
        const val LowestPrice = "lowest_price"
    }

}