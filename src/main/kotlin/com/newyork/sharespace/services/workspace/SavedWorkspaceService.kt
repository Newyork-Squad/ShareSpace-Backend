package com.newyork.sharespace.services.workspace

import com.newyork.sharespace.api.dto.workspace.SavedWorkspaceResponse
import com.newyork.sharespace.api.dto.workspace.toWorkspaceResponse
import com.newyork.sharespace.repository.SavedWorkspaceRepository
import com.newyork.sharespace.repository.UserRepository
import com.newyork.sharespace.repository.WorkspaceRepository
import com.newyork.sharespace.services.entity.SavedWorkspace
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SavedWorkspaceService(
    private val savedWorkspaceRepository: SavedWorkspaceRepository,
    private val workspaceRepository: WorkspaceRepository,
    private val userRepository: UserRepository
) {
    fun saveWorkspace(userId: UUID, workspaceId: UUID): SavedWorkspace {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow { IllegalArgumentException("Workspace not found") }

        if (savedWorkspaceRepository.existsByUserAndWorkspace(user, workspace)) {
            throw IllegalStateException("Workspace already saved")
        }

        val saved = SavedWorkspace(user = user, workspace = workspace)
        return savedWorkspaceRepository.save(saved)
    }

    fun getSavedWorkspaces(userId: UUID, pageable: Pageable): Page<SavedWorkspaceResponse> {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        return savedWorkspaceRepository.findByUser(user, pageable)
            .map { SavedWorkspaceResponse(it.id, it.workspace.toWorkspaceResponse(), it.createdAt) }
    }

    @Transactional
    fun removeSavedWorkspace(userId: UUID, workspaceId: UUID) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val workspace = workspaceRepository.findById(workspaceId)
            .orElseThrow { IllegalArgumentException("Workspace not found") }

        savedWorkspaceRepository.deleteByUserAndWorkspace(user, workspace)
    }
}