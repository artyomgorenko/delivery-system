package ru.delivery.system.controller;

import ru.delivery.system.dao.UserManager;
import ru.delivery.system.model.json.UserInfoResponse;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static ru.delivery.system.common.utils.JsonSerializer.toJson;

@Path("user/")
public class UserController {

    @EJB
    private UserManager userManager;

    @POST
    @Path("addUser/")
    public Response login(String json) {
        return null;
    }

    @POST// or GET?
    @Path("getUserInfo/")
    public Response getUserInfo() {
        return null;
    }


    @GET
    @Path("getAllUsers/")
    @Transactional
    public Response getAllUsers() {
        try {
            List<UserInfoResponse> usersInfo = userManager.getAllUsers()
                    .stream().map(userEntity -> {
                        UserInfoResponse userInfoResponse = new UserInfoResponse();
                        userInfoResponse.setId(userEntity.getId());
                        userInfoResponse.setUsername(userEntity.getUsername());
                        userInfoResponse.setName(userEntity.getName());
                        userInfoResponse.setSurname(userEntity.getSurname());
                        userInfoResponse.setRole(userEntity.getRole());
                        userInfoResponse.setLastLatitude(userEntity.getLastLatitude());
                        userInfoResponse.setLastLongitude(userEntity.getLastLongitude());
                        return  userInfoResponse;
                    }).collect(Collectors.toList());

            return Response.status(Response.Status.OK).entity(toJson(usersInfo)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }
}