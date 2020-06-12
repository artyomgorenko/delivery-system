package ru.delivery.system.model.json.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.delivery.system.model.json.BaseRequest;

public class ProductCreationRequest extends BaseRequest {

    @JsonProperty("body")
    @Getter @Setter
    private Body body;

    public static class Body {
        @JsonProperty("name")
        @Getter @Setter
        private String name;

        @JsonProperty("category")
        @Getter @Setter
        private String category;

        @JsonProperty("weight")
        @Getter @Setter
        private Double weight;

        @JsonProperty("cost")
        @Getter @Setter
        private Double cost;

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
