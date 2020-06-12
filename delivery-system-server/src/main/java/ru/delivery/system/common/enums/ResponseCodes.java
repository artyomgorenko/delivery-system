package ru.delivery.system.common.enums;


import lombok.Getter;
import lombok.Setter;

public enum ResponseCodes {
    OK("", 0),
    API_ERROR("", -1);

    ResponseCodes(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Getter @Setter
    private String message;
    @Getter @Setter
    private Integer code;
}
