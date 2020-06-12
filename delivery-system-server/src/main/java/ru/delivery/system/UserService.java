package ru.delivery.system;

import lombok.Getter;
import lombok.Setter;

import javax.ejb.Stateless;

@Stateless
public class UserService {

    public String getUserById() {
        return "User";
    }

}
