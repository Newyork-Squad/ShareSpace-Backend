package com.newyork.sharespace.repository

import com.newyork.sharespace.services.entity.SavedWorkspace
import com.newyork.sharespace.services.entity.User
import com.newyork.sharespace.services.entity.Workspace
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SavedWorkspaceRepository : JpaRepository<SavedWorkspace, UUID> {
    fun findByUser(user: User, pageable: Pageable): Page<SavedWorkspace>
    fun existsByUserAndWorkspace(user: User, workspace: Workspace): Boolean
    fun deleteByUserAndWorkspace(user: User, workspace: Workspace)
}