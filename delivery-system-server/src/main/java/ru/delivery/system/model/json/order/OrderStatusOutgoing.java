package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.json.BaseResponse;
import ru.delivery.system.model.other.GeoPoint;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusOutgoing extends BaseResponse {

    @JsonProperty("body")
    @Getter @Setter
    private Body body;

    @JsonInclude(JsonInclude.Include.NON_NULL)
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

        /**
         * Values only for DONE status
         */
        @JsonProperty("startPoint")
        @Getter @Setter
        private GeoPoint startPoint;

        @JsonProperty("warehousePoint")
        @Getter @Setter
        private GeoPoint warehousePoint;

        @JsonProperty("deliveryPoint")
        @Getter @Setter
        private GeoPoint deliveryPoint;

        @JsonProperty("startDate")
        @Getter @Setter
        private Date startDate;

        @JsonProperty("startShipmentDate")
        @Getter @Setter
        private Date startShipmentDate;

        @JsonProperty("startDeliveringDate")
        @Getter @Setter
        private Date startDeliveringDate;

        @JsonProperty("doneDate")
        @Getter @Setter
        private Date doneDate;

        @JsonProperty("wareHouseRouteLength")
        @Getter @Setter
        private Integer wareHouseRouteLength;

        @JsonProperty("deliveryRouteLength")
        @Getter @Setter
        private Double deliveryRouteLength;

        @JsonProperty("totalCost")
        @Getter @Setter
        private Double totalCost;
    }

}
