package ru.delivery.system.controller;

import ru.delivery.system.common.utils.JsonSerializer;
import ru.delivery.system.dao.MapMarkerManager;
import ru.delivery.system.model.entities.MapMarkerEntity;
import ru.delivery.system.model.json.MapMarkerIncoming;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("map/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MapController {

    @EJB
    private MapMarkerManager markerManager;

    @POST
    @Path("/addMarker")
    public Response addMarker(String json) {
        try {
            MapMarkerIncoming mapMarker = JsonSerializer.toEntity(json, MapMarkerIncoming.class);

            markerManager.createMarker(mapMarker);

            String jsonMessage = "{\"StatusMessage\":\"Coordinates Applied\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    @GET
    @Path("/getMarkers")
    public Response getMarkers() {
        try {
            List<MapMarkerEntity> mapMarkers = markerManager.getMapMarkers();
            return Response.status(Response.Status.OK).entity(JsonSerializer.toJson(mapMarkers)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }
}
