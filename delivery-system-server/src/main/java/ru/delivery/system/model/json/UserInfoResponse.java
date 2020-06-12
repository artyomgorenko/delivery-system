package ru.delivery.system.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


public class UserInfoResponse {
    @JsonProperty("id")
    @Getter @Setter
    private Integer id;

    @JsonProperty("username")
    @Getter @Setter
    private String username;

    @JsonProperty("name")
    @Getter @Setter
    private String name;

    @JsonProperty("surname")
    @Getter @Setter
    private String surname;

    @JsonProperty("role")
    @Getter @Setter
    private String role;

    @JsonProperty("lastLatitude")
    @Getter @Setter
    private Float lastLatitude;

    @JsonProperty("lastLongitude")
    @Getter @Setter
    private Float lastLongitude;
}
