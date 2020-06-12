package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.other.GeoPoint;

/**
 * Body запроса на добавление точки к маршруту заказа
 */
public class RoutePointIncoming {
    @JsonProperty("orderId")
    @Getter @Setter
    private Integer orderId;

    @JsonProperty("driverId")
    @Getter @Setter
    private Integer driverId;

    @JsonProperty("serialNumber")
    @Getter @Setter
    private Integer serialNumber;

    @JsonProperty("geoPoint")
    @Getter @Setter
    private GeoPoint geoPoint;
}
