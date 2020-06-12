package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product", schema = "public", catalog = "delivery_system")
public class ProductEntity {

    @Id
    @Column(name = "p_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_PRODUCT")
    @SequenceGenerator(name="GEN_PRODUCT", sequenceName = "PRODUCT_P_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private Integer id;

    @Basic
    @Column(name = "p_name")
    @Getter @Setter
    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "p_category", referencedColumnName = "c_id", nullable = false)
    @Getter @Setter
    private CategoryEntity category;

    @Basic
    @Column(name = "p_weight")
    @Getter @Setter
    private Double weight;

    @Basic
    @Column(name = "p_cost")
    @Getter @Setter
    private Double cost;

    @Basic
    @Column(name = "p_height")
    @Getter @Setter
    private Double height;

    @Basic
    @Column(name = "p_width")
    @Getter @Setter
    private Double width;

    @Basic
    @Column(name = "p_length")
    @Getter @Setter
    private Double length;

}
