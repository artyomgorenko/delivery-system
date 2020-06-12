package ru.delivery.system.model.json.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProductListResponse {
    @JsonProperty(value = "productList")
    @Getter
    @Setter
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

        @JsonProperty("category")
        @Getter @Setter
        private String category;

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
