package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public abstract class BaseRequest {

    @JsonProperty("header")
    @Getter @Setter
    private Header header;

    public static class Header {
        @JsonProperty("userCode")
        @Getter @Setter
        private String userCode;

        @JsonProperty("requestTime")
        @Getter @Setter
        private Date requestTime;
    }

}
