package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.other.GeoPoint;

/**
 * Body запроса на добавление точки к маршруту заказа
 */
public class RoutePointIncoming {
    @JsonProperty(value = "orderId")
    @Getter @Setter
    private Integer orderId;

    @JsonProperty(value = "serialNumber")
    @Getter @Setter
    private Integer serialNumber;

    @JsonProperty(value = "geoPoint")
    @Getter @Setter
    private GeoPoint geoPoint;
}
