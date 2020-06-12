package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_details", schema = "public", catalog = "delivery_system")
public class OrderDetailsEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_ORDER_DETAILS")
    @SequenceGenerator(name="GEN_ORDER_DETAILS", sequenceName = "ORDER_DETAILS_OD_ID_SEQ", allocationSize = 1)
    @Column(name = "od_id")
    @Getter @Setter
    private int odId;

    @ManyToOne
    @JoinColumn(name = "odo_id", referencedColumnName = "o_id", nullable = false)
    @Getter @Setter
    private OrderEntity order;
}
