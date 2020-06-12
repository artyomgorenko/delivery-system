package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class OrderOutgoing {
    @JsonProperty(value = "orderId")
    @Getter @Setter
    private Integer orderId;
}
