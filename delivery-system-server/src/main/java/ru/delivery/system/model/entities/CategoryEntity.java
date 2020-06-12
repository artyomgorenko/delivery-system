package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "p_category", schema = "public", catalog = "delivery_system")
public class CategoryEntity {
    @Id
    @Column(name = "c_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_CATEGORY")
    @SequenceGenerator(name="GEN_CATEGORY", sequenceName = "P_CATEGORY_C_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private Integer cId;

    @Basic
    @Column(name = "c_name")
    @Getter @Setter
    private String cName;

    @Basic
    @Column(name = "c_description")
    @Getter @Setter
    private String cDescription;

}
