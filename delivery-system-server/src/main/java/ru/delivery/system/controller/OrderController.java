package ru.delivery.system.controller;

import ru.delivery.system.dao.MapRouteManager;
import ru.delivery.system.dao.OrderManager;
import ru.delivery.system.model.entities.MapRouteEntity;
import ru.delivery.system.model.entities.MapRoutePointEntity;
import ru.delivery.system.model.entities.OrderEntity;
import ru.delivery.system.model.json.OrderIncoming;
import ru.delivery.system.model.json.OrderMarkerIncoming;
import ru.delivery.system.model.json.OrderOutgoing;
import ru.delivery.system.model.json.OrderRouteOutgoing;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static ru.delivery.system.common.JsonSerializer.toEntity;
import static ru.delivery.system.common.JsonSerializer.toJson;

@Path("order/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {

    @EJB
    private OrderManager orderManager;
    @EJB
    private MapRouteManager mapRouteManager;

    @POST
    @Path("/addOrder")
    public Response addOrder(String json) {
        try {
            OrderIncoming orderIncoming = toEntity(json, OrderIncoming.class);

            if (orderIncoming.getOrderDate() == null) {
                throw new RuntimeException("Дата заказа не задана");
            }

            OrderEntity orderEntity = orderManager.createOrder(orderIncoming);

            OrderOutgoing orderOutgoing = new OrderOutgoing();
            orderOutgoing.setOrderId(orderEntity.getOId());
            return Response.status(Response.Status.OK).entity(toJson(orderOutgoing)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    @POST
    @Path("/addOrderMarker")
    public Response addOrderMarker(String json) {
        try {
            OrderMarkerIncoming orderMarker = toEntity(json, OrderMarkerIncoming.class);

            MapRouteEntity mapRouteEntity = mapRouteManager.getMapRouteByOrderId(orderMarker.getOrderNumber());
            if (mapRouteEntity == null) {
                throw new RuntimeException("Маршрут для заказа с номером " + orderMarker.getOrderNumber() + " не найден");
            }

            mapRouteManager.createMapRoutePoint(mapRouteEntity, orderMarker);

            return Response.status(Response.Status.OK).entity(null).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    @GET
    @Path("/getOrderRoute")
    public Response getOrderRoute(@QueryParam("orderId") Integer orderId) {
        try {
            MapRouteEntity mapRouteEntity = mapRouteManager.getMapRouteByOrderId(orderId);
            if (mapRouteEntity == null) {
                throw new RuntimeException("Не найден маршурт для заказа с Id = " + orderId);
            }

            List<MapRoutePointEntity> mapRoutePoints = mapRouteEntity.getMapRoutePoints();
            List<OrderRouteOutgoing.RoutePoint> routePoints = new ArrayList<>();
            for (MapRoutePointEntity pointEntity: mapRoutePoints) {
                OrderRouteOutgoing.RoutePoint routePoint = new OrderRouteOutgoing.RoutePoint();
                routePoint.setSerialNumber(pointEntity.getMrpSerialNumber());
                routePoint.setLatitude(pointEntity.getMrpLatitude());
                routePoint.setLongitude(pointEntity.getMrpLongitude());
                routePoints.add(routePoint);
            }
            OrderRouteOutgoing orderRouteOutgoing = new OrderRouteOutgoing();
            orderRouteOutgoing.setOrderId(orderId);
            orderRouteOutgoing.setRoutePoints(routePoints);

            return Response.status(Response.Status.OK).entity(toJson(orderRouteOutgoing)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

}
