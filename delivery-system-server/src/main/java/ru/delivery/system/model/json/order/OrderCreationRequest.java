package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class OrderCreationRequest {

    @JsonProperty("createDate")
    @Getter @Setter
    private Date createDate;

    @JsonProperty("status")
    @Getter @Setter
    private String status;
    
    @JsonProperty("driverId")
    @Getter @Setter
    private Integer driverId;

    @JsonProperty("transportId")
    @Getter @Setter
    private Integer transportId;

    /**
     * Адрес пункта отправления
     */
    @JsonProperty("departurePoint")
    @Getter @Setter
    private String departurePoint;

    @JsonProperty("departureLatitude")
    @Getter @Setter
    private Float departureLatitude;

    @JsonProperty("departureLongitude")
    @Getter @Setter
    private Float departureLongitude;

    /**
     * Адрес пункта прибытия
     */
    @JsonProperty("destinationPoint")
    @Getter @Setter
    private String destinationPoint;

    @JsonProperty("destinationLatitude")
    @Getter @Setter
    private Float destinationLatitude;
    @JsonProperty("destinationLongitude")
    @Getter @Setter
    private Float destinationLongitude;

    /**
     * Список продуктов в заказе
     */
    @JsonProperty("productList")
    @Getter @Setter
    private List<Product> productList;

    public static class Product {
        @JsonProperty("productId")
        @Getter @Setter
        private Integer productId;

        @JsonProperty("count")
        @Getter @Setter
        private Integer count;

        @JsonProperty("cost")
        @Getter @Setter
        private Double cost;

        @JsonProperty("name")
        @Getter @Setter
        private String name;

        @JsonProperty("weight")
        @Getter @Setter
        private Double weight;

        @JsonProperty("height")
        @Getter @Setter
        private Double height;

        @JsonProperty("width")
        @Getter @Setter
        private Double width;

        @JsonProperty("length")
        @Getter @Setter
        private Double length;
    }

}
