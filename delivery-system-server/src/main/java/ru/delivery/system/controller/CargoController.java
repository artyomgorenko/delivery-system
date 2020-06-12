package ru.delivery.system.controller;


import org.hibernate.mapping.Collection;
import ru.delivery.system.dao.CargoManager;
import ru.delivery.system.dao.OrderManager;
import ru.delivery.system.model.entities.CargoEntity;
import ru.delivery.system.model.entities.MovementsEntity;
import ru.delivery.system.model.entities.MovementsManager;
import ru.delivery.system.model.json.cargo.CargoInfoResponse;
import ru.delivery.system.model.json.product.ProductListResponse;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.delivery.system.common.utils.JsonSerializer.toJson;

@Path("cargo/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CargoController {
    @EJB
    private CargoManager cargoManager;
    @EJB
    private MovementsManager movementsManager;
    @EJB
    private OrderManager orderManager;

    @GET
    @Path("/cargoList")
    @Transactional
    public Response getProductList() {
        try {
            CargoInfoResponse cargoInfoResponse = new CargoInfoResponse();

            List<CargoInfoResponse.Cargo> cargoList = new ArrayList<>();
            for (CargoEntity cargoEntity : cargoManager.getCargoList()) {
                CargoInfoResponse.Cargo cargo = constructCargoInfo(cargoEntity);
                if (!cargo.getMovementsList().isEmpty())
                    cargoList.add(cargo);
            }
            cargoInfoResponse.setCargoList(cargoList);

            return Response.status(Response.Status.OK).entity(toJson(cargoInfoResponse)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    private CargoInfoResponse.Cargo constructCargoInfo(CargoEntity cargoEntity) {
        CargoInfoResponse.Cargo cargo = new CargoInfoResponse.Cargo();
        cargo.setCargoId(cargoEntity.getPId());
        cargo.setProductId(cargoEntity.getProduct().getId());
        cargo.setStatus(cargoEntity.getStatus());
        cargo.setWarehouseId(1); // TODO: add warehouseId column to products table
        cargo.setOrderIdList(orderManager.getCargoOrdersIds(cargoEntity));
        cargo.setMovementsList(
                movementsManager.getCargoMovements(cargoEntity).stream()
                .map(movementsEntity -> {
                    CargoInfoResponse.Cargo.Movement movement = new CargoInfoResponse.Cargo.Movement();
                    movement.setMovementId(movementsEntity.getMId());
                    movement.setPrevStatus(movementsEntity.getPrevStatus());
                    movement.setNewStatus(movementsEntity.getNewStatus());
                    movement.setDate(movementsEntity.getMDate());
                    return movement;
                }).collect(Collectors.toList())
        );
        return cargo;
    }

}
