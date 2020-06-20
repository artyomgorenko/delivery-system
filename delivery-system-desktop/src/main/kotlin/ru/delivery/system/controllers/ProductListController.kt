package ru.delivery.system.controllers

import ru.delivery.system.common.JsonSerializer
import ru.delivery.system.models.json.ProductListEntity
import ru.delivery.system.rest.HttpHelper

object ProductListController {

    private val httpHelper = HttpHelper
    private val jsonSerializer = JsonSerializer()

    var productList: List<ProductListEntity.Product> = ArrayList()

    init {
        try {
            val resp = httpHelper.syncGet("product/productList")
            resp.use { response ->
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val productListEntity = jsonSerializer.toEntity<ProductListEntity>(body.string())
                        productList = productListEntity.productList!!
                        println("Initialize product list. Size=${productList.size}")
                    }
                } else {
                    println("Failed to initialize product list")
                }
            }
        } catch (e: Exception) {
            println("Product list initialization error: ${e.message}")
        }
    }
}