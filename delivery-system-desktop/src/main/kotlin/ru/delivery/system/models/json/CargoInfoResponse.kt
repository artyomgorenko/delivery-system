package ru.delivery.system.models.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.ArrayList

class CargoInfoResponse {

    @JsonProperty("cargoList")
    var cargoList: List<Cargo>? = null


}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Cargo {

    /**
     * Id самого товара
     */
    @JsonProperty("cargoId")
    var cargoId: Int? = null

    /**
     * Id артикула продукции
     */
    @JsonProperty("productId")
    var productId: Int? = null

    /**
     * Номер склада, на котором хранится товар
     */
    @JsonProperty("warehouseId")
    var warehouseId: Int? = null

    /**
     * Список номеров заказов, в которых учавствовал товар
     */
    @JsonProperty("orderIdList")
    var orderIdList: List<Int> = ArrayList()

    /**
     * Стаутс товара
     *
     * @see ProductStatus
     */
    @JsonProperty("status")
    var status: String? = null

    /**
     * История изменений статустов конкретного товара
     */
    @JsonProperty("movementsList")
    var movementsList: List<Movement> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Movement {
        @JsonProperty("movementId")
        var movementId: Int? = null

        @JsonProperty("newStatus")
        var newStatus: String? = null

        @JsonProperty("prevStatus")
        var prevStatus: String? = null

        @JsonProperty("date")
        var date: Date? = null
    }
}
