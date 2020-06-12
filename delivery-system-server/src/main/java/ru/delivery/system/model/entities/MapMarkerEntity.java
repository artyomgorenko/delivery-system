package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for map testing purposes
 */
@Entity
@Table(name = "MAP_MARKERS")
public class MapMarkerEntity {

    @Id
    @Column(name = "M_ID")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_MAP_MARKERS")
    @SequenceGenerator(name="GEN_MAP_MARKERS", sequenceName = "MAP_MARKERS_M_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private int markerId;

    @Column(name = "M_LATITUDE")
    @Getter @Setter
    private Double latitude;

    @Column(name = "M_LONGITUDE")
    @Getter @Setter
    private Double longitude;
}