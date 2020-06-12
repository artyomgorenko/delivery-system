package ru.delivery.system.model.json.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.other.GeoPoint;

public class RoutePoint {
    @JsonProperty(value = "serialNumber")
    @Getter
    @Setter
    private Integer serialNumber;

    @JsonProperty(value = "geoPoint")
    @Getter @Setter
    private GeoPoint geoPoint;
}
