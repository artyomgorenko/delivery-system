package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "warehouse", schema = "public", catalog = "delivery_system")
public class WarehouseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_WAREHOUSE")
    @SequenceGenerator(name="GEN_WAREHOUSE", sequenceName = "WAREHOUSE_W_ID_SEQ", allocationSize = 1)
    @Column(name = "w_id")
    @Getter @Setter
    private Integer wId;

    @Basic
    @Column(name = "w_address")
    @Getter @Setter
    private String wAddress;

    @Basic
    @Column(name = "w_latitude")
    @Getter @Setter
    private Float wLatitude;

    @Basic
    @Column(name = "w_longitude")
    @Getter @Setter
    private Float wLongitude;

}
