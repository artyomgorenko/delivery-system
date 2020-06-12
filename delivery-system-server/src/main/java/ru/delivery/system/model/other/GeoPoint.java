package ru.delivery.system.model.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class GeoPoint {
    @JsonProperty(value = "latitude")
    @Getter @Setter
    private Float latitude;

    @JsonProperty(value = "longitude")
    @Getter @Setter
    private Float longitude;
}
