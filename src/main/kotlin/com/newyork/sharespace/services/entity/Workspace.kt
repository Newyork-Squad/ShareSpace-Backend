package com.newyork.sharespace.services.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID



@Entity
@Table(name = "workspaces")
data class Workspace(
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val description: String,

    @Column(nullable = false)
    val location: String,

    @Column(nullable = false)
    val latitude: Double,

    @Column(nullable = false)
    val longitude: Double,

    @Column(nullable = false)
    val price: Double,

    @ElementCollection(targetClass = Amenities::class)
    @CollectionTable(name = "workspace_amenities", joinColumns = [JoinColumn(name = "workspace_id")])
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity")
    val amenities: Set<Amenities> = emptySet(),

    @Column(nullable = false)
    val owner: UUID,

    @Column(name = "image_urls", columnDefinition = "TEXT", nullable = true)
    val images: String?,

    @Column(columnDefinition = "TEXT", nullable = true)
    val reviews: String?,

    @Column(nullable = true)
    val rateId: UUID?,

    @Column(name = "reviews_count", nullable = true)
    val reviewsNo: Int?,

    @Column(nullable = true)
    val capacity: Int?,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val availability: Boolean = true,

    @Column(name = "house_rules", columnDefinition = "TEXT", nullable = true)
    val houseRules: String?
)

enum class Amenities {
    WI_FI,
    AIR_CONDITIONING,
    POWER_BACKUP,
    WHITEBOARD,
    PROJECTOR,
    PRINTER,
    COFFEE_MACHINE,
    PARKING,
    MEETING_ROOM,
    LOUNGE_AREA,
    KITCHENETTE,
    WATER_DISPENSER,
    RESTROOM_ACCESS,
    SECURE_WIFI,
    WHEELCHAIR_ACCESSIBLE,
    FIRE_SAFETY,
    LOCKABLE_ROOM,
    CCTV_SECURITY,
    BIKE_STAND,
    NEAR_PUBLIC_TRANSPORT
}

