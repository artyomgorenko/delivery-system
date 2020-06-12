package ru.delivery.system.model.json.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Создание нового заказа
 */
public class OrderIncoming {

    @JsonProperty(value = "createDate")
    @Getter @Setter
    private Date createDate;

    @JsonProperty(value = "driverId")
    @Getter @Setter
    private Integer driverId;

    @JsonProperty(value = "transportId")
    @Getter @Setter
    private Integer transportId;

    @JsonProperty(value = "departurePoint")
    @Getter @Setter
    private String departurePoint;

    @JsonProperty(value = "destinationPoint")
    @Getter @Setter
    private String destinationPoint;

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

    }

}
