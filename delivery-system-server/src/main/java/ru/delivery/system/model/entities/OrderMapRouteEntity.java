package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_map_route", schema = "public", catalog = "delivery_system")
@IdClass(OrderMapRouteEntityPK.class)
public class OrderMapRouteEntity {
    @Id
    @Column(name = "o_id")
    @Getter
    @Setter
    private Integer oId;

    @Id
    @Column(name = "mr_id")
    @Getter @Setter
    private Integer mrId;

    @ManyToOne
    @JoinColumn(name = "o_id", referencedColumnName = "o_id", nullable = false, insertable = false, updatable = false)
    @Getter @Setter
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "mr_id", referencedColumnName = "mr_id", nullable = false, insertable = false, updatable = false)
    @Getter @Setter
    private MapRouteEntity mapRouteEntity;
}
