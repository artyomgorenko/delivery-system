package ru.delivery.system.controller;

import ru.delivery.system.common.enums.OrderStatus;
import ru.delivery.system.common.enums.ProductStatus;
import ru.delivery.system.dao.*;
import ru.delivery.system.model.entities.*;
import ru.delivery.system.model.json.RoutePointIncoming;
import ru.delivery.system.model.json.common.RoutePoint;
import ru.delivery.system.model.json.order.*;
import ru.delivery.system.model.other.GeoPoint;

import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.delivery.system.common.utils.GeoUtils.calacRouteDistance;
import static ru.delivery.system.common.utils.JsonSerializer.toEntity;
import static ru.delivery.system.common.utils.JsonSerializer.toJson;
import static ru.delivery.system.common.utils.MapUtils.distanceAsDouble;

@Path("order/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {

    @EJB
    private OrderManager orderManager;
    @EJB
    private MapRouteManager mapRouteManager;
    @EJB
    private UserManager userManager;
    @EJB
    private TransportManager transportManager;
    @EJB
    private StockentryManager stockentryManager;
    @EJB
    private MovementsManager movementsManager;

    @POST
    @Path("/addOrder")
    public Response addOrder(String json) {
        try {
            OrderIncoming orderIncoming = toEntity(json, OrderIncoming.class);

            if (orderIncoming.getCreateDate() == null) {
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

    @GET
    @Path("/getOrderInfo")
    @Transactional
    public Response getOrderRoute(@QueryParam("orderIdList") List<Integer> orderIdList) {
        try {
            // TODO: возможно стоит сделать более легковесный пакет для данных маршрута
            List<OrderInfoOutgoing> orderInfoOutgoings = new ArrayList<>();
            for (Integer orderId: orderIdList) {
                OrderEntity orderEntity = orderManager.getOrderById(orderId);
                if (orderEntity == null) {
                    throw new RuntimeException("Не найден заказ с Id = " + orderId);
                }
                orderInfoOutgoings.add(constructOrderOutgoing(orderEntity));
            }

            return Response.status(Response.Status.OK).entity(toJson(orderInfoOutgoings)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    @GET
    @Path("/getAllOrders")
    @Transactional
    public Response getAllOrders() {
        try {
            List<OrderInfoOutgoing> orders = constructOrderOutgoingList(orderManager.getOrders());
            return Response.status(Response.Status.OK).entity(toJson(orders)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    @GET
    @Path("/getNewOrders")
    @Transactional
    public Response getNewOrders() {
        try {
            List<OrderInfoOutgoing> orders = constructOrderOutgoingList(orderManager.getNewOrders());
            return Response.status(Response.Status.OK).entity(toJson(orders)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    @GET
    @Path("/getDriverOrders")
    @Transactional
    public Response getNewOrders(@QueryParam("") int driverId) {
        try {
            UserEntity user = userManager.getUserById(driverId);
            if (user == null) {
                error("Пользователь с id=" + driverId + " не найден");
            }
            List<OrderInfoOutgoing> orders = constructOrderOutgoingList(orderManager.getDriverOrders(user));
            return Response.status(Response.Status.OK).entity(toJson(orders)).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    private List<OrderInfoOutgoing> constructOrderOutgoingList(List<OrderEntity> orderEntities) {
        return orderEntities.stream()
                .map(this::constructOrderOutgoing)
                .collect(Collectors.toList());
    }

    private OrderInfoOutgoing constructOrderOutgoing(OrderEntity orderEntity) {
        OrderInfoOutgoing orderInfoOutgoing = new OrderInfoOutgoing();
        orderInfoOutgoing.setOrderId(orderEntity.getOId());
        orderInfoOutgoing.setCreateDate(orderEntity.getCreateDate());
        orderInfoOutgoing.setStatus(orderEntity.getStatus());
        orderInfoOutgoing.setTransportId(orderEntity.getTransport() == null ? null : orderEntity.getTransport().getId());
        orderInfoOutgoing.setDriverId(orderEntity.getUser() == null ? null : orderEntity.getUser().getId());
        orderInfoOutgoing.setDeparturePoint(orderEntity.getDeparturePoint());
        orderInfoOutgoing.setDestinationPoint(orderEntity.getDestinationPoint());

        // Route
        if (!orderEntity.getOrderMapRoutes().isEmpty()) {
            OrderInfoOutgoing.Route route = new OrderInfoOutgoing.Route();
            List<MapRoutePointEntity> mapRoutePoints = orderEntity.getOrderMapRoutes().get(0).getMapRouteEntity().getMapRoutePoints();
            List<RoutePoint> routePoints = new ArrayList<>();

            for (MapRoutePointEntity pointEntity : mapRoutePoints) {
                RoutePoint routePoint = new RoutePoint();
                GeoPoint geoPoint = new GeoPoint();
                routePoint.setSerialNumber(pointEntity.getMrpSerialNumber());
                geoPoint.setLatitude(pointEntity.getMrpLatitude());
                geoPoint.setLongitude(pointEntity.getMrpLongitude());
                routePoint.setGeoPoint(geoPoint);
                routePoints.add(routePoint);
            }
            route.setRoutePoints(routePoints);
            List<GeoPoint> geoPoints = routePoints.stream().map(RoutePoint::getGeoPoint).collect(Collectors.toList());
            route.setRouteDistance(calacRouteDistance(geoPoints));
            orderInfoOutgoing.setOrderRoute(route);
        }

        // Product list
        List<OrderInfoOutgoing.Product> productList = new ArrayList<>();
        for (OrderDetailsEntity orderDetail : orderEntity.getOrderDetails()) {
            OrderInfoOutgoing.Product product = new OrderInfoOutgoing.Product();
            ProductEntity productEntity = orderDetail.getProduct().getProduct();
            product.setProductId(productEntity.getId());
            product.setCount(orderDetail.getCount());
            product.setCost(productEntity.getCost());
            product.setName(productEntity.getName());
            product.setWeight(productEntity.getWeight());
            product.setHeight(productEntity.getHeight());
            product.setWidth(productEntity.getWidth());
            product.setLength(productEntity.getLength());
            productList.add(product);
        }
        orderInfoOutgoing.setProductList(productList);

        return orderInfoOutgoing;
    }

    @POST
    @Path("/changeOrderStatus")
    @Transactional
    public Response changeOrderStatus(String json) {
        OrderStatusOutgoing orderStatusOutgoing = new OrderStatusOutgoing();
        OrderStatusOutgoing.Header respHeader = new OrderStatusOutgoing.Header();
        OrderStatusOutgoing.Body respBody = new OrderStatusOutgoing.Body();
        orderStatusOutgoing.setHeader(respHeader);
        orderStatusOutgoing.setBody(respBody);

        try {
            OrderStatusIncoming orderStatusIncoming = toEntity(json, OrderStatusIncoming.class);
            OrderStatusIncoming.Body requestBody = orderStatusIncoming.getBody();
            respBody.setOrderId(requestBody.getOrderId());

            OrderEntity orderEntity = orderManager.getOrderById(requestBody.getOrderId());
            if (orderEntity == null) {
                error("Заказ с номером " + requestBody.getOrderId() + " не найден");
            }
            UserEntity userEntity = userManager.getUserById(requestBody.getDriverId());
            if (userEntity == null) {
                error("Водитель с id " + requestBody.getDriverId() + " не найден");
            }
            if (!"DRIVER".equals(userEntity.getRole())) {
                error("Пользователь с id " + requestBody.getDriverId() + " не является водителем");
            }

            TransportEntity transportEntity = transportManager.getTransportById(requestBody.getTransportId());
            if (transportEntity == null) {
                error("Транспорт с id " + requestBody.getTransportId() + " не зарегестрирован в базе");
            }

            if (OrderStatus.PRODUCT_PICKING.isEqual(requestBody.getNewStatus())) {
                if (!OrderStatus.NEW.isEqual(orderEntity.getStatus())) {
                    error("Заказ не в статусе NEW");
                }
                if (orderEntity.getUser() != null) {
                    error("Заказ закреплен за другим водителем");
                }
                orderEntity.setStatus(OrderStatus.PRODUCT_PICKING.name());
                orderEntity.setTransport(transportEntity);
                orderEntity.setUser(userEntity);
                orderEntity.setStartDate(new Timestamp(new Date().getTime()));

                respHeader.setMessage("Заказ взят в работу");
            }
            else if (OrderStatus.PRODUCT_SHIPMENT.isEqual(requestBody.getNewStatus())) {
                if (OrderStatus.PRODUCT_PICKING.isEqual(orderEntity.getStatus())) {
                    orderEntity.setStatus(OrderStatus.PRODUCT_SHIPMENT.name());
                    orderEntity.setStartShipmentDate(new Timestamp(new Date().getTime()));
                } else if (OrderStatus.CANCELED.isEqual(orderEntity.getStatus())) {
                    // Возврат после доставки. Переводим товары в статус ACTIVE
                    for (OrderDetailsEntity orderDetail : orderEntity.getOrderDetails()) {
                        movementsManager.makeMovement(orderDetail.getProduct(), ProductStatus.ACTIVE);
                    }
                } else {
                    error("Товар уже был отгружен, или заказ еще не взят в работу");
                }

            }
            else if (OrderStatus.DELIVERING.isEqual(requestBody.getNewStatus())) {
                if (OrderStatus.PRODUCT_SHIPMENT.isEqual(orderEntity.getStatus())) {
                    orderEntity.setStatus(OrderStatus.DELIVERING.name());
                    orderEntity.setStartDeliveringDate(new Timestamp(new Date().getTime()));

                    for (OrderDetailsEntity orderDetail : orderEntity.getOrderDetails()) {

                        // Меняем статус заказов на отгружены
                        movementsManager.makeMovement(orderDetail.getProduct(), ProductStatus.OFFLOADED);

                        // Пока без списания с остатков...
//                    Integer productId = orderDetail.getProduct().getId();
//                    Integer warehouseId = orderDetail.getProduct().getId();
//                    stockentryManager.writeoffProduct(productId, warehouseId, orderDetail.getCount());
                    }
                } else if (OrderStatus.CANCELED.isEqual(orderEntity.getStatus())) {
                    // Возврат после доставки. Переводим товары в статус ACTIVE
                    for (OrderDetailsEntity orderDetail : orderEntity.getOrderDetails()) {
                        movementsManager.makeMovement(orderDetail.getProduct(), ProductStatus.ACTIVE);
                    }
                } else {
                    error("Нельзя начать доставку. Неверный статус заказа");
                }
            }
            else if (OrderStatus.DONE.isEqual(requestBody.getNewStatus())) {
//                if (!OrderStatus.DELIVERING.isEqual(orderEntity.getStatus()) ||
//                    !OrderStatus.CANCELED.isEqual(orderEntity.getStatus())) {
//                    error("Заказ не может быть завершен. Неверный статус(" + orderEntity.getStatus() + ")");
//                }
                orderEntity.setStatus(OrderStatus.DONE.name());
                orderEntity.setDoneDate(new Timestamp(new Date().getTime()));
                respBody.setDistanceInMeters(100F);
                respBody.setDeliveryTimeMs(100L);

                // Переводим товары в статус DONE
                for (OrderDetailsEntity orderDetail : orderEntity.getOrderDetails()) {
                    movementsManager.makeMovement(orderDetail.getProduct(), ProductStatus.DONE);
                }

                // Вычисление длины пути доставки
                if (!orderEntity.getOrderMapRoutes().isEmpty()) {
                    List<MapRoutePointEntity> routePoints = orderEntity.getOrderMapRoutes().get(0).getMapRouteEntity().getMapRoutePoints();
                    GeoPoint prevPoint = null;
                    Double deliveryDistance = 0.0;
                    for (MapRoutePointEntity routePoint: routePoints) {
                        if (prevPoint == null) {
                            prevPoint = new GeoPoint();
                            prevPoint.setLatitude(routePoint.getMrpLatitude());
                            prevPoint.setLongitude(routePoint.getMrpLongitude());
                            continue;
                        }

                        //TODO: возможно не стоит плодить новые объекты
                        GeoPoint currentPoint = new GeoPoint();
                        currentPoint.setLatitude(prevPoint.getLatitude());
                        currentPoint.setLongitude(prevPoint.getLongitude());
                        prevPoint = new GeoPoint();
                        prevPoint.setLatitude(routePoint.getMrpLatitude());
                        prevPoint.setLongitude(routePoint.getMrpLongitude());

                        deliveryDistance += distanceAsDouble(prevPoint, currentPoint);
                    }
                    respBody.setDeliveryRouteLength(deliveryDistance);
                }
                Date startDate = orderEntity.getStartDate();
                Date startShipmentDate = orderEntity.getStartShipmentDate();
                Date startDeliveringDate = orderEntity.getStartDeliveringDate();
                Date doneDate = orderEntity.getDoneDate();
                Long time = doneDate.getTime() - startDate.getTime();
                Double totalCost = time.doubleValue() / 1000 / 60 / 60 * 150;

                respBody.setStartDate(orderEntity.getStartDate());
                respBody.setStartShipmentDate(orderEntity.getStartShipmentDate());
                respBody.setStartDeliveringDate(orderEntity.getStartDeliveringDate());
                respBody.setTotalCost(totalCost);
                respBody.setDoneDate(orderEntity.getDoneDate());
                respHeader.setMessage("Заказ завершен");
            }
            else if (OrderStatus.CANCELED.isEqual(requestBody.getNewStatus())) {
                if (OrderStatus.DONE.isEqual(orderEntity.getStatus())) {
                    error("Заказ уже завршен и не может быть отменен");
                }

                orderEntity.setStatus(OrderStatus.CANCELED.name());
                respHeader.setMessage("Заказ отменен");
            }
            else {
                error("Неверный статус(" + orderStatusIncoming.getBody().getNewStatus() + ")");
            }

            orderManager.save(orderEntity);
            respHeader.setResultCode(0);
            return createOkResponse(orderStatusOutgoing);
        } catch (RuntimeException e) {
            respHeader.setResultCode(-1);
            respHeader.setMessage(e.getMessage());
            return createOkResponse(orderStatusOutgoing);
        } catch (Exception ex) {
            String jsonMessage = "{\"Error\":\"" + ex.getMessage() + "\"}";
            return createOkResponse(jsonMessage);
        }
    }

    /**
     * Добавление точки маршрута для заказа и обновления последнего местоположения водителя,
     * либо только обновление местоположения
     */
    @POST
    @Path("/addRoutePoint")
    public Response addRoutePoint(String json) {
        try {
            RoutePointIncoming routePointIncoming = toEntity(json, RoutePointIncoming.class);

            if (routePointIncoming.getOrderId() != null) {
                OrderEntity orderEntity = orderManager.getOrderById(routePointIncoming.getOrderId());
                if (orderEntity == null) {
                    error("Маршрут для заказа с номером " + routePointIncoming.getOrderId() + " не найден");
                }

                mapRouteManager.createRoutePoint(orderEntity.getOId(), routePointIncoming);
            }

            UserEntity userEntity = userManager.getUserById(routePointIncoming.getDriverId());
            if (userEntity == null) {
                error("Пользователь с id=" + routePointIncoming.getDriverId() + " не найден");
            }
            // update last driver position
            userEntity.setLastLatitude(routePointIncoming.getGeoPoint().getLatitude());
            userEntity.setLastLongitude(routePointIncoming.getGeoPoint().getLongitude());
            userManager.save(userEntity);

            return Response.status(Response.Status.OK).entity(null).build();
        } catch (Exception e) {
            String jsonMessage = "{\"Error\":\"" + e.getMessage() + "\"}";
            return Response.status(Response.Status.OK).entity(jsonMessage).build();
        }
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }

    private Response createOkResponse(Object entity) {
        return Response.status(Response.Status.OK).entity(entity).build();
    }

}
