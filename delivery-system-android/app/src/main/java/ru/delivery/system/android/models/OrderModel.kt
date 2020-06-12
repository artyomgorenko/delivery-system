package ru.delivery.system.android.models

object OrderModel {
    var orderData: OrderData? = OrderData(999, "####")

    /**
     * Статусы для отображения маршрута на карте:
     * - PRODUCT_PICKING - Движение до склада. Отображается путь до склада и до конечной точки. Учет пути по заказу не ведется
     * - DELIVERING - Непосредственно доставка товара до покупателя. Отображается путь до конечной точки. Ведется учет пройденного пути
     */
    var deliveryStatus: String = "PRODUCT_PICKING"

    /**
     * Сбрасывает модель до начального состояния
     */
    fun clearModel() {
        orderData = null
        deliveryStatus = "PRODUCT_PICKING"
    }

}