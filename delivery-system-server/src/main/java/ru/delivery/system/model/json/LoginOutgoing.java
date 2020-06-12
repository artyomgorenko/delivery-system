package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class LoginOutgoing {

    @JsonProperty(value = "userId")
    @Getter @Setter
    private String userId;

    @JsonProperty(value = "name")
    @Getter @Setter
    private String name;

    @JsonProperty(value = "surname")
    @Getter @Setter
    private String surname;

    /**
     * User role: driver/dispatcher
     */
    @JsonProperty(value = "role")
    @Getter @Setter
    private String role;

    @JsonProperty(value = "appAdmin")
    @Getter @Setter
    private boolean appAdmin;

    /**
     * TODO: Not used yet. SessionId validation must be implemented in the future
     */
    @JsonProperty(value = "sessionId")
    @Getter @Setter
    private String sessionId;

    @JsonProperty(value = "errorMessage")
    @Getter @Setter
    private String errorMessage;
}
