package ru.delivery.system.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StockentryEntityPK implements Serializable {

    @Id
    @Column(name = "sw_id")
    @Getter @Setter
    private Integer swId;

    @Id
    @Column(name = "sp_id")
    @Getter @Setter
    private Integer spId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockentryEntityPK that = (StockentryEntityPK) o;
        return Objects.equals(swId, that.swId) &&
                Objects.equals(spId, that.spId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(swId, spId);
    }
}
