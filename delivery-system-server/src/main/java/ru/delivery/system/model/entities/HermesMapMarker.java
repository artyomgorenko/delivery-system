package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity for map testing purposes
 */
@Entity
@Table(name = "map_markers")
public class HermesMapMarker {

    @Id
    @Column(name = "M_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int markerId;

    @Column(name = "M_LATITUDE")
    @Getter @Setter
    private Double latitude;

    @Column(name = "M_LONGITUDE")
    @Getter @Setter
    private Double longitude;
}