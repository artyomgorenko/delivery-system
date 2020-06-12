package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class OrderStatusIncoming {
    @JsonProperty("body")
    @Getter @Setter
    private Body body;

    public static class Body {
        @JsonProperty(value = "orderId")
        @Getter @Setter
        private Integer orderId;

        @JsonProperty(value = "driverId")
        @Getter @Setter
        private Integer driverId;

        @JsonProperty(value = "transportId")
        @Getter @Setter
        private Integer transportId;

        @JsonProperty(value = "newStatus")
        @Getter @Setter
        private String newStatus;
    }

}
