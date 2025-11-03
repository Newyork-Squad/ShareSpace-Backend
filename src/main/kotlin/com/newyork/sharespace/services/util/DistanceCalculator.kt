package com.newyork.sharespace.services.util

import kotlin.math.*

 fun calculateDistanceInKm(
    userLatitude: Double,
    userLongitude: Double,
    workspaceLatitude: Double,
    workspaceLongitude: Double
): Double {
    val earthRadiusKm = 6371.0

    val latitudeDifference = Math.toRadians(workspaceLatitude - userLatitude)
    val longitudeDifference = Math.toRadians(workspaceLongitude - userLongitude)

    val haversineFormula = sin(latitudeDifference / 2).pow(2) +
            cos(Math.toRadians(userLatitude)) * cos(Math.toRadians(workspaceLatitude)) *
            sin(longitudeDifference / 2).pow(2)

    val centralAngle = 2 * atan2(sqrt(haversineFormula), sqrt(1 - haversineFormula))

    return earthRadiusKm * centralAngle
}
