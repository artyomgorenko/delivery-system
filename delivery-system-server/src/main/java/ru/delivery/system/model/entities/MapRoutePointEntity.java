package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "map_route_point", schema = "public", catalog = "delivery_system")
@IdClass(MapRoutePointEntityPK.class)
public class MapRoutePointEntity {

    @Id
    @Column(name = "mrp_mr_id")
    @Getter @Setter
    private Integer mrpMrId;

    @Id
    @Column(name = "mrp_serial_number")
    @Getter @Setter
    private Integer mrpSerialNumber;

    @Basic
    @Column(name = "mrp_latitude")
    @Getter @Setter
    private Float mrpLatitude;

    @Basic
    @Column(name = "mrp_longitude")
    @Getter @Setter
    private Float mrpLongitude;

    @ManyToOne
    @JoinColumn(name = "mrp_mr_id", referencedColumnName = "mr_id", nullable = false, insertable = false, updatable = false)
    @Getter @Setter
    private MapRouteEntity mapRouteByMrpMrId;
}
