package ru.delivery.system.android.models

import ru.delivery.system.android.models.OrderStatus.CANCELED_STATUS
import ru.delivery.system.android.models.OrderStatus.DELIVERING_STATUS
import ru.delivery.system.android.models.OrderStatus.DONE_STATUS
import ru.delivery.system.android.models.OrderStatus.PRODUCT_PICKING_STATUS
import ru.delivery.system.android.models.OrderStatus.SHIPMENT_STATUS

object OrderModel {
    var orderData: OrderData? = null/*OrderData(999, "####")*/

    /**
     * Статусы для отображения маршрута на карте:
     * - PRODUCT_PICKING - Движение до склада. Отображается путь до склада и до конечной точки. Учет пути по заказу не ведется
     * - DELIVERING - Непосредственно доставка товара до покупателя. Отображается путь до конечной точки. Ведется учет пройденного пути
     */
    var status: String = "PRODUCT_PICKING"

    /**
     * Сбрасывает модель до начального состояния
     */
    fun clearModel() {
        orderData = null
        status = "PRODUCT_PICKING"
    }

    /**
     * Начинается путь на склад. Заказ меняет статус на PRODUCT_PICKING
     */
    fun getOrederInWork(orderData: OrderData) {
        status = PRODUCT_PICKING_STATUS
        this.orderData = orderData
    }

    /**
     * Начало отгрузки товаров
     */
    fun startProductsShipment() {
        status = SHIPMENT_STATUS
    }

    /**
     * Подтверждение отгрузки отваров. Начало доставки
     */
    fun startDelivering() {
        status = DELIVERING_STATUS
    }

    /**
     * Завершение заказа
     */
    fun finishOrder() {
        status = DONE_STATUS
    }

    /**
     * Завершение заказа
     */
    fun cancelOrder() {
        status = CANCELED_STATUS
    }

}

object OrderStatus {
    const val PRODUCT_PICKING_STATUS = "PRODUCT_PICKING" // Путь к складу
    const val DELIVERING_STATUS = "DELIVERING" // Доставка от склада до места назначения
    const val SHIPMENT_STATUS = "PRODUCT_SHIPMENT" // Отгрузка товара
    const val DONE_STATUS = "DONE" // Заказ завершен
    const val CANCELED_STATUS = "CANCELED" // Заказ завершен
}
