package ru.delivery.system.model.json.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Обертка для добавления новой точки маршрута для заказа
 */
public class OrderMarkerIncoming {

    @Getter @Setter
    @JsonProperty(value = "orderNumber")
    private Integer orderNumber;

    @Getter @Setter
    @JsonProperty(value = "serialNumber")
    private Integer serialNumber;

    @Getter @Setter
    @JsonProperty(value = "latitude")
    private Float latitude;

    @Getter @Setter
    @JsonProperty(value = "longitude")
    private Float longitude;

}
