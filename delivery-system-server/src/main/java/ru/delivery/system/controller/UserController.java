package ru.delivery.system.controller;

import ru.delivery.system.dao.UserManager;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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

}