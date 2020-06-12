package ru.delivery.system.model.json.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.json.common.RoutePoint;
import ru.delivery.system.model.other.GeoPoint;

import java.util.Date;
import java.util.List;

/**
 * Полная информация о заказе, включая список товаров и маршрут
 */
public class OrderInfoOutgoing {

    @JsonProperty(value = "orderId")
    @Getter @Setter
    private Integer orderId;

    @JsonProperty(value = "createDate")
    @Getter @Setter
    private Date createDate;

    @JsonProperty(value = "status")
    @Getter @Setter
    private String status;

    @JsonProperty(value = "driverId")
    @Getter @Setter
    private Integer driverId;

    @JsonProperty(value = "transportId")
    @Getter @Setter
    private Integer transportId;

    @JsonProperty(value = "departurePoint")
    @Getter @Setter
    private String departurePoint;

    @JsonProperty(value = "departureLongitude")
    @Getter @Setter
    private Double departureLongitude;

    @JsonProperty(value = "departureLatitude")
    @Getter @Setter
    private Double departureLatitude;

    @JsonProperty(value = "destinationPoint")
    @Getter @Setter
    private String destinationPoint;

    @JsonProperty(value = "destinationLongitude")
    @Getter @Setter
    private Double destinationLongitude;

    @JsonProperty(value = "destinationLatitude")
    @Getter @Setter
    private Double destinationLatitude;

    /**
     * Список продуктов в заказе
     */
    @JsonProperty(value = "productList")
    @Getter @Setter
    private List<Product> productList;

    public static class Product {
        @JsonProperty(value = "productId")
        @Getter @Setter
        private Integer productId;

        @JsonProperty(value = "count")
        @Getter @Setter
        private Integer count;

        @JsonProperty(value = "cost")
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

    /**
     * Маршрут доставки заказа
     */
    @JsonProperty(value = "orderRoute")
    @Getter @Setter
    private Route orderRoute;

    public static class Route {
        @JsonProperty(value = "routeDistance")
        @Getter @Setter
        private Double routeDistance;

        @JsonProperty(value = "routePoints")
        @Getter @Setter
        private List<RoutePoint> routePoints;
    }
}
