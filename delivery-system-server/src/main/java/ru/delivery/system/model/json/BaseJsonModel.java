package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseJsonModel {
    @JsonProperty("header")
    @Getter @Setter
    private Header header;

    public static class Header {
        @JsonProperty("resultCode")
        @Getter @Setter
        private Integer resultCode;

        @JsonProperty("message")
        @Getter @Setter
        private String message;
    }

}
