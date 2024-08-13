package com.myongjiway.buslocation

data class BusLocation(
    val busId: String,
    val latitude: Double,
    val longitude: Double,
    val direction: Int,
    val timestamp: Long,
)
