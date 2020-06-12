package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Заказ
 */
@Entity
@Table(name = "order_", schema = "public", catalog = "delivery_system")
public class OrderEntity {
    // Основная инфа о заказе (Дата, номер, статус(джавовый enum))
    // Строки заказа
    // Стоимость доставки
    // Водитель
    // Машина

    @Id
    @Column(name = "o_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_ORDER")
    @SequenceGenerator(name="GEN_ORDER", sequenceName = "ORDER__O_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private Integer oId;

    @Getter @Setter
    @Column(name = "o_createdate")
    private Timestamp createDate;

    @Getter @Setter
    @OneToMany(mappedBy = "order")
    private Collection<OrderDetailsEntity> orderDetails;

    @Getter @Setter
    @OneToMany(mappedBy = "mapRouteEntity")
    private Collection<OrderMapRouteEntity> orderMapRoutes;
}