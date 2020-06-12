package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Строка заказа
 */
@Entity
@Table(name = "order_details", schema = "public", catalog = "delivery_system")
public class OrderDetailsEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_ORDER_DETAILS")
    @SequenceGenerator(name="GEN_ORDER_DETAILS", sequenceName = "ORDER_DETAILS_OD_ID_SEQ", allocationSize = 1)
    @Column(name = "od_id")
    @Getter @Setter
    private int odId;

    /**
     * Количество товара
     */
    @Getter @Setter
    @Column(name = "od_count")
    private Integer count;

    /**
     * Ссылка на заказ
     */
    @ManyToOne
    @JoinColumn(name = "odo_id", referencedColumnName = "o_id", nullable = false)
    @Getter @Setter
    private OrderEntity order;

    /**
     * Ссылка на продукт
     */
    @ManyToOne
    @JoinColumn(name = "od_product_id", referencedColumnName = "p_id")
    @Getter @Setter
    private ProductEntity product;
}
