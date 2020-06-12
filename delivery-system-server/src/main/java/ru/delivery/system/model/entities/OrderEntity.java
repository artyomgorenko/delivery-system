package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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
    @Column(name = "o_departure_point")
    private String departurePoint;

    @Basic
    @Column(name = "o_departure_latitude")
    @Getter @Setter
    private Float departureLatitude;

    @Basic
    @Column(name = "o_departure_longitude")
    @Getter @Setter
    private Float departureLongitude;

    @Getter @Setter
    @Column(name = "o_destination_point")
    private String destinationPoint;

    @Basic
    @Column(name = "o_destination_latitude")
    @Getter @Setter
    private Float destinationLatitude;

    @Basic
    @Column(name = "o_destination_longitude")
    @Getter @Setter
    private Float destinationLongitude;

    @Getter @Setter
    @Column(name = "O_STATUS")
    private String status;

    @Getter @Setter
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetailsEntity> orderDetails;

    @Getter @Setter
    @OneToMany(mappedBy = "mapRouteEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderMapRouteEntity> orderMapRoutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "o_driver_id", referencedColumnName = "u_id", nullable = false)
    @Getter @Setter
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "o_transport_id", referencedColumnName = "t_id", nullable = false)
    @Getter @Setter
    private TransportEntity transport;

}