package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "movments", schema = "public", catalog = "delivery_system")
public class MovementsEntity {
    @Id
    @Column(name = "m_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_MOVMENTS")
    @SequenceGenerator(name="GEN_MOVMENTS", sequenceName = "MOVMENTS_M_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private Integer mId;

    @Basic
    @Column(name = "new_status")
    @Getter @Setter
    private String newStatus;

    @Basic
    @Column(name = "prev_status")
    @Getter @Setter
    private String prevStatus;

    @Basic
    @Column(name = "m_date")
    @Getter @Setter
    private Timestamp mDate;

    /**
     * Ссылка на продукт
     */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "p_id", referencedColumnName = "p_id")
    @Getter @Setter
    private CargoEntity product;
}
