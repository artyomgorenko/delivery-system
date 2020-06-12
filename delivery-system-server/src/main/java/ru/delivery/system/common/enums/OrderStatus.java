package ru.delivery.system.common.enums;

public enum OrderStatus {
    NEW,
    PRODUCT_PICKING, // На пути к складу
    PRODUCT_SHIPMENT, // Отгрузка товара со склада
    DELIVERING, // Доставки после отгрузки товара со склада
    DONE, // Доставка завершена
    CANCELED; // Доставка отменена

    public boolean isEqual(String status) {
        return this.name().equalsIgnoreCase(status);
    }
}
