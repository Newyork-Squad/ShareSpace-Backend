package com.newyork.sharespace.services.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "workspace_ratings")
data class WorkspaceRating(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val workspaceId: UUID,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val rating: Int
)