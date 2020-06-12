package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "map_route", schema = "public", catalog = "delivery_system")
public class MapRouteEntity {

    @Id
    @Column(name = "mr_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_MAP_ROUTE")
    @SequenceGenerator(name="GEN_MAP_ROUTE", sequenceName = "MAP_ROUTE_MR_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private int mrId;

    @Basic
    @Column(name = "mr_departure_point")
    @Getter @Setter
    private String departurePoint;

    @Basic
    @Column(name = "mr_destination_point")
    @Getter @Setter
    private String destinationPoint;

    @Basic
    @Column(name = "mr_length")
    @Getter @Setter
    private Integer routeLength;

    @Getter @Setter
    @OneToMany(mappedBy = "mapRouteEntity")
    private List<OrderMapRouteEntity> orderMapRoutes;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "mrp_mr_id", referencedColumnName = "mr_id", nullable = false, insertable = false, updatable = false)
    @Getter @Setter
    private List<MapRoutePointEntity> mapRoutePoints;

}
