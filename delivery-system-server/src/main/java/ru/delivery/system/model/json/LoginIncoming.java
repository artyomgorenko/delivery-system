package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class LoginIncoming {
    @JsonProperty(value = "username")
    @Getter @Setter
    private String username;

    @JsonProperty(value = "password")
    @Getter @Setter
    private String password;
}
