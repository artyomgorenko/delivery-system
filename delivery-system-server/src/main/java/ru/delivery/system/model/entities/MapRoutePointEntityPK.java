package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class MapRoutePointEntityPK implements Serializable {
    @Id
    @Column(name = "mrp_mr_id")
    @Getter @Setter
    private Integer mrpMrId;

    @Id
    @Column(name = "mrp_serial_number")
    @Getter @Setter
    private Integer mrpSerialNumber;

}
