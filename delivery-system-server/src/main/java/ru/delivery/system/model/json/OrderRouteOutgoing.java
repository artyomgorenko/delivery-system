package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class OrderRouteOutgoing {

    @JsonProperty(value = "orderId")
    @Getter @Setter
    private Integer orderId;

    @JsonProperty(value = "routePoints")
    @Getter @Setter
    private List<RoutePoint> routePoints;

    public static class RoutePoint {
        @JsonProperty(value = "serialNumber")
        @Getter @Setter
        private Integer serialNumber;

        @JsonProperty(value = "latitude")
        @Getter @Setter
        private Float latitude;

        @JsonProperty(value = "longitude")
        @Getter @Setter
        private Float longitude;
    }
}
