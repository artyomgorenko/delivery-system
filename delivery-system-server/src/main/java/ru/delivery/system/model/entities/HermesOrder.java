package ru.delivery.system.model.entities;

import javax.persistence.Table;

/**
 * Заказ
 */
@Table(name = "ORDER", schema = "DELIVERY_SYSTEM_DB")
public class HermesOrder {
    // Основная инфа о заказе (Дата, номер, статус(джавовый enum))
    // Строки заказа
    // Стоимость доставки
    // Водитель
    // Машина
}