package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.json.BaseJsonModel;

public class OrderStatusOutgoing extends BaseJsonModel {

    @JsonProperty("body")
    @Getter @Setter
    private Body body;

    public static class Body {
        @JsonProperty("orderId")
        @Getter @Setter
        private Integer orderId;

        @JsonProperty("distanceInMeters")
        @Getter @Setter
        private Float distanceInMeters;

        @JsonProperty("deliveryTimeMs")
        @Getter @Setter
        private Long deliveryTimeMs;
    }

}
