package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "transport", schema = "public", catalog = "delivery_system")
public class TransportEntity {

    @Id
    @Column(name = "t_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "GEN_TRANSPORT")
    @SequenceGenerator(name="GEN_TRANSPORT", sequenceName = "TRANSPORT_T_ID_SEQ", allocationSize = 1)
    @Getter @Setter
    private Integer id;

    @Basic
    @Column(name = "t_number")
    @Getter @Setter
    private String number;

    @Basic
    @Column(name = "t_model")
    @Getter @Setter
    private String model;

    @Basic
    @Column(name = "t_car_body_capacity")
    @Getter @Setter
    private Double carBodyCapacity;

    @Basic
    @Column(name = "t_fuel_consumption")
    @Getter @Setter
    private Double fuelConsumption;

}
