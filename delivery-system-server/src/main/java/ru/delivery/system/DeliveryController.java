package ru.delivery.system;


import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo")
public class DeliveryController {

    @EJB
    private UserService userService;

    @GET
    @Path("/settings")
    @Produces(MediaType.APPLICATION_XML)
    public Response settings() {
        return Response.status(Response.Status.OK).entity("<tag>Hello, " + userService.getUserById() + "!</tag>").build();
    }

}
