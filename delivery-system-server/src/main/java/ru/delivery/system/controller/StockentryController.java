package ru.delivery.system.controller;

import ru.delivery.system.dao.StockentryManager;
import ru.delivery.system.model.json.LoginOutgoing;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static ru.delivery.system.common.utils.JsonSerializer.toEntity;
import static ru.delivery.system.common.utils.JsonSerializer.toJson;

@Path("stockentry/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class StockentryController {

    @EJB
    private StockentryManager stockentryManager;

    @GET
    @Path("getStockentries/")
    public Response getStockentries() {
        LoginOutgoing loginOutgoing = new LoginOutgoing();
        try {
            return Response.status(Response.Status.OK).entity(toJson(loginOutgoing)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }
}
