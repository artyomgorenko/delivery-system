package ru.delivery.system.controller;

import ru.delivery.system.dao.UserManager;
import ru.delivery.system.model.entities.UsersEntity;
import ru.delivery.system.model.json.LoginIncoming;
import ru.delivery.system.model.json.LoginOutgoing;


import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import static ru.delivery.system.common.JsonSerializer.toEntity;
import static ru.delivery.system.common.JsonSerializer.toJson;

@Path("user/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginController {

    @EJB
    private UserManager userManager;

    @POST
    @Path("login/")
    public Response addOrder(String json) {
        LoginOutgoing loginOutgoing = new LoginOutgoing();
        try {
            LoginIncoming loginIncoming = toEntity(json, LoginIncoming.class);
            String username = loginIncoming.getUsername();
            String password = loginIncoming.getPassword();

            UsersEntity usersEntity = userManager.findUser(username, password);
            if (usersEntity == null) {
                loginOutgoing.setErrorMessage("Неверная пара логин-пароль");
            } else if (loginOutgoing.getErrorMessage() == null) {
                loginOutgoing.setName(usersEntity.getName());
                loginOutgoing.setSurname(usersEntity.getSurname());
                loginOutgoing.setRole(usersEntity.getRole());
                loginOutgoing.setSessionId(UUID.randomUUID().toString());
                loginOutgoing.setAppAdmin(false);
            }
            return Response.status(Response.Status.OK).entity(toJson(loginOutgoing)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }
}
