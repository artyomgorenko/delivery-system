package ru.delivery.system.android.models

import org.osmdroid.util.GeoPoint

data class OrderData(
    var id: Int,
    var description: String,
    var departmentAddress: String = "Краснодар Уральская 112",
    var destinationAddress: String = "Краснодар Северная 15",
    var departmentPoint: GeoPoint = GeoPoint(45.0339, 39.0978),
    var destinationPoint: GeoPoint = GeoPoint(45.05, 39.0998),
    var deliveryTime: String = "12:00"
)