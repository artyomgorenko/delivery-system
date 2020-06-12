package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class OrderConfirmOutgoing {
    @JsonProperty("orderId")
    @Getter @Setter
    private Integer orderId;

    @JsonProperty("driverId")
    @Getter @Setter
    private Integer driverId;

    /**
     * 0, -1
     */
    @JsonProperty("statusCode")
    @Getter @Setter
    private Integer statusCode;

    @JsonProperty("distance")
    @Getter @Setter
    private Float distance;

    @JsonProperty("cost")
    @Getter @Setter
    private Float deliveryCost;

    @JsonProperty("deliveryTimeMs")
    @Getter @Setter
    private Long deliveryTimeMs;
}
