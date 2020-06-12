package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class WarehousesInfoResponse {
    @JsonProperty("id")
    @Getter @Setter
    private Integer id;

    @JsonProperty("address")
    @Getter @Setter
    private String address;

    @JsonProperty("latitude")
    @Getter @Setter
    private Float latitude;

    @JsonProperty("longitude")
    @Getter @Setter
    private Float longitude;
}
