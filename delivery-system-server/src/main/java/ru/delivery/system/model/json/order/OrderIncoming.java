package ru.delivery.system.model.json.order;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Создание нового заказа
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderIncoming {

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

    @JsonProperty("deliveryDate")
    @Getter @Setter
    private Date deliveryDate;

    @JsonProperty("isOrderCommon")
    @Getter @Setter
    private Boolean isOrderCommon;

    @JsonProperty("deliveryUrgency")
    @Getter @Setter
    private String deliveryUrgency;

    @JsonProperty("baseCost")
    @Getter @Setter
    private Float baseCost;

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

    }


}
