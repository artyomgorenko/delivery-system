package ru.delivery.system.controllers

import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.json.Cargo
import ru.delivery.system.models.json.CargoInfoResponse
import ru.delivery.system.rest.HttpHelper

/**
 *
 */
object CargoListController {
    private val httpHelper = HttpHelper
    private val jsonSerializer = JsonSerializer()

    var cargoList: List<Cargo> = ArrayList()

    init {
        updateCargoList()
    }

    fun updateCargoList() {
        try {
            val resp = httpHelper.syncGet("cargo/cargoList")
            resp.use { response ->
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val cargoInfoList = jsonSerializer.toEntity<CargoInfoResponse>(body.string())
                        cargoInfoList.cargoList?.let { cargoList = it }
                        println("Initialize cargo list. Size=${cargoList.size}")
                    }
                } else {
                    println("Failed to INITIALIZE cargo list")
                }
            }
        } catch (e: Exception) {
            println("Cargo list initialization error: ${e.message}")
        }
    }
}