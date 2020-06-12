package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.other.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class OrderRouteOutgoing {

    @JsonProperty(value = "orderId")
    @Getter @Setter
    private Integer orderId;

    @JsonProperty(value = "routePoints")
    @Getter @Setter
    private List<RoutePoint> routePoints;

    @JsonProperty(value = "routeDistance")
    @Getter @Setter
    private Double routeDistance;

    public static class RoutePoint {
        @JsonProperty(value = "serialNumber")
        @Getter @Setter
        private Integer serialNumber;

        @JsonProperty(value = "geoPoint")
        @Getter @Setter
        private GeoPoint geoPoint;
    }
}
