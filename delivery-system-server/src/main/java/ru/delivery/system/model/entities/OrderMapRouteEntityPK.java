package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class OrderMapRouteEntityPK implements Serializable {
    @Id
    @Column(name = "o_id")
    @Getter @Setter
    private int oId;

    @Id
    @Column(name = "mr_id")
    @Getter @Setter
    private int mrId;
}
