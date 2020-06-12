package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonProperty

class ProductListEntity {
    @JsonProperty("productList")
    var productList: List<Product>? = null

    class Product {
        @JsonProperty("productId")
        var productId: Int? = null

        @JsonProperty("count")
        var count: Int? = null

        @JsonProperty("cost")
        var cost: Double? = null

        @JsonProperty("name")
        var name: String? = null

        @JsonProperty("category")
        var category: String? = null

        @JsonProperty("weight")
        var weight: Double? = null

        @JsonProperty("height")
        var height: Double? = null

        @JsonProperty("width")
        var width: Double? = null

        @JsonProperty("length")
        var length: Double? = null
    }
}
