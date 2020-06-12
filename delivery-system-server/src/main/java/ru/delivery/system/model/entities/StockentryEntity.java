package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "stockentry", schema = "public", catalog = "delivery_system")
@IdClass(StockentryEntityPK.class)
public class StockentryEntity {

    @Id
    @Column(name = "sw_id")
    @Getter @Setter
    private Integer swId;

    @Id
    @Column(name = "sp_id")
    @Getter @Setter
    private Integer spId;

    @Basic
    @Column(name = "s_quantity")
    @Getter @Setter
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "sw_id", referencedColumnName = "w_id", nullable = false, insertable = false, updatable = false)
    @Getter @Setter
    private WarehouseEntity warehouse;

    @ManyToOne
    @JoinColumn(name = "sp_id", referencedColumnName = "p_id", nullable = false, insertable = false, updatable = false)
    @Getter @Setter
    private ProductEntity product;

}
