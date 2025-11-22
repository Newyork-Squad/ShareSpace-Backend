package com.newyork.sharespace.services.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "workspace_reviews",
    uniqueConstraints = [UniqueConstraint(columnNames = ["workspace_id", "user_id"])]
)
data class WorkspaceReview(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val workspaceId: UUID,

    @Column(nullable = false)
    val userId: UUID,

    @Column(nullable = false, length = 2000)
    val comment: String,

    @Column(nullable = false)
    val rating: Int,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
