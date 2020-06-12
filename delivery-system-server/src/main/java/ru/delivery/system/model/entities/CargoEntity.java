package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "products", schema = "public", catalog = "delivery_system")
public class CargoEntity {
    @Id
    @Column(name = "p_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_PRODUCTS")
    @SequenceGenerator(name="GEN_PRODUCTS", sequenceName = "PRODUCTS_P_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private Integer pId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pp_id", referencedColumnName = "p_id", nullable = false)
    @Getter @Setter
    private ProductEntity product;

    @Basic
    @Column(name = "status")
    @Getter @Setter
    private String status;

}
