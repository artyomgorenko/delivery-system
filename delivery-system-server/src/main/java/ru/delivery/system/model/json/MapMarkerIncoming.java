package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class MapMarkerIncoming {
    @Getter
    @Setter
    @JsonProperty(value = "latitude")
    private Double latitude;

    @Getter
    @Setter
    @JsonProperty(value = "longitude")
    private Double longitude;
}
