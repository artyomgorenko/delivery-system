package ru.delivery.system.controller;

import ru.delivery.system.dao.WarehouseManager;
import ru.delivery.system.model.entities.WarehouseEntity;
import ru.delivery.system.model.json.WarehousesInfoResponse;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

import static ru.delivery.system.common.utils.JsonSerializer.toJson;

@Path("warehouse/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WarehouseController {

    @EJB
    private WarehouseManager warehouseManager;

    @GET
    @Path("/getAllWarehouses")
    public Response getAllWarehouses() {
        try {
            List<WarehouseEntity> warehouseEntities = warehouseManager.getAllWarehouses();
            List<WarehousesInfoResponse> response = warehouseEntities.stream().map(warehouse -> {
                WarehousesInfoResponse warehousesInfo = new WarehousesInfoResponse();
                warehousesInfo.setId(warehouse.getWId());
                warehousesInfo.setAddress(warehouse.getWAddress());
                warehousesInfo.setLatitude(warehouse.getWLatitude());
                warehousesInfo.setLongitude(warehouse.getWLongitude());
                return warehousesInfo;
            }).collect(Collectors.toList());

            return Response.status(Response.Status.OK).entity(toJson(response)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }
}
