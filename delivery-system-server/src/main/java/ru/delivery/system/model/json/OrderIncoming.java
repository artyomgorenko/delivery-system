package ru.delivery.system.model.json;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Создание нового заказа
 */
public class OrderIncoming {

    /**
     * Пункт отправления
     */
    @JsonProperty
    @Getter @Setter
    private String departurePoint;

    /**
     * Пункт прибытия
     */
    @JsonProperty
    @Getter @Setter
    private String destinationPoint;

    /**
     * Дата создания заказа
     */
    @JsonProperty
    @Getter @Setter
    private Date orderDate;


}
