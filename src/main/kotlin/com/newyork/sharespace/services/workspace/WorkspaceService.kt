package com.newyork.sharespace.services.workspace

import com.newyork.sharespace.api.dto.AddWorkspaceRequest
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


    private companion object {
        const val Popular = "popular"
        const val TopRated = "top_rated"
        const val LowestPrice = "lowest_price"
    }

}