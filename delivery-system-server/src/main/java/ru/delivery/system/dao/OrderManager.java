package ru.delivery.system.dao;

import ru.delivery.system.common.enums.OrderStatus;
import ru.delivery.system.common.enums.ProductStatus;
import ru.delivery.system.model.entities.*;
import ru.delivery.system.model.json.order.OrderIncoming;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.List;

@Stateless
public class OrderManager {

    @EJB
    private UserManager userManager;
    @EJB
    private TransportManager transportManager;
    @EJB
    private ProductManager productManager;
    @EJB
    private CargoManager cargoManager;
    @EJB
    private MovementsManager movementsManager;

    @PersistenceContext(name = "PostgresDS")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrderEntity orderEntity) {
        em.merge(orderEntity);
        em.flush();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OrderEntity createOrder(OrderIncoming orderIncoming) {
        TransportEntity transportEntity = null;
        UserEntity userEntity = null;
        if (orderIncoming.getTransportId() != null && orderIncoming.getDriverId() != null) {
            transportEntity = transportManager.getTransportById(orderIncoming.getTransportId());
            if (transportEntity == null) {
                throw new RuntimeException("Не найдена машина с id = " + orderIncoming.getTransportId());
            }
            userEntity = userManager.getUserById(orderIncoming.getDriverId());
            if (userEntity == null) {
                throw new RuntimeException("Не найден водитель с id = " + orderIncoming.getTransportId());
            }
        }

        if (orderIncoming.getProductList() == null) {
            throw new RuntimeException("В заказе не задан список товаров");
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateDate(new Timestamp(orderIncoming.getCreateDate().getTime()));
        
        orderEntity.setDeparturePoint(orderIncoming.getDeparturePoint());
        orderEntity.setDepartureLongitude(orderIncoming.getDepartureLongitude());
        orderEntity.setDepartureLatitude(orderIncoming.getDepartureLatitude());
        
        orderEntity.setDestinationPoint(orderIncoming.getDestinationPoint());
        orderEntity.setDestinationLongitude(orderIncoming.getDestinationLongitude());
        orderEntity.setDestinationLatitude(orderIncoming.getDestinationLatitude());
        
        orderEntity.setStatus(OrderStatus.NEW.name());
        orderEntity.setType(orderIncoming.getIsOrderCommon() ? "COMMON" : "INTERNAL");
        orderEntity.setTransport(transportEntity);
        orderEntity.setUser(userEntity);
        orderEntity.setBaseCost(orderIncoming.getBaseCost());
        em.persist(orderEntity);

        // Order details
        for (OrderIncoming.Product product : orderIncoming.getProductList()) {
            // Резервируем товары
            List<CargoEntity> productsEntities =
                    cargoManager.getCargosForReserve(product.getProductId(), product.getCount());
            // Создаем детали заказа
            for (CargoEntity cargoEntity : productsEntities) {
                movementsManager.makeMovement(cargoEntity, ProductStatus.REZERVED);
                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setProduct(cargoEntity);
                orderDetailsEntity.setOrder(orderEntity);
//                orderDetailsEntity.setCount(product.getCount());
                em.persist(orderDetailsEntity);
            }
        }

        MapRouteEntity mapRouteEntity = new MapRouteEntity();
        mapRouteEntity.setDeparturePoint(orderIncoming.getDeparturePoint());
        mapRouteEntity.setDestinationPoint(orderIncoming.getDeparturePoint());
        em.persist(mapRouteEntity);

        OrderMapRouteEntity orderMapRouteEntity = new OrderMapRouteEntity();
        orderMapRouteEntity.setOId(orderEntity.getOId());
        orderMapRouteEntity.setMrId(mapRouteEntity.getMrId());
        em.persist(orderMapRouteEntity);

        em.flush();
        return orderEntity;
    }

    @TransactionAttribute
    public OrderEntity getOrderById(Integer orderId) {
        return em.createQuery("select o from OrderEntity o where o.oId = :orderId", OrderEntity.class)
                .setParameter("orderId", orderId)
                .getResultList().stream().findFirst().orElse(null);
    }

    @TransactionAttribute
    public List<OrderEntity> getOrders() {
        return em.createQuery("select o from OrderEntity o", OrderEntity.class)
                .getResultList();
    }

    @TransactionAttribute
    public List<OrderEntity> getNewOrders() {
        return em.createQuery("select o from OrderEntity o " +
                "where o.status=:orderStatus", OrderEntity.class)
                .setParameter("orderStatus", OrderStatus.NEW.toString())
                .getResultList();
    }

    @TransactionAttribute
    public List<OrderEntity> getDriverOrders(UserEntity user) {
        return em.createQuery("select o from OrderEntity o " +
                "where o.user=:user", OrderEntity.class)
                .setParameter("user", user)
                .getResultList();
    }

    @TransactionAttribute
    public List<Integer> getCargoOrdersIds(CargoEntity cargoEntity) {
        return em.createQuery("select o.id from OrderEntity o " +
                "join  o.orderDetails od " +
                "where od.product.id=:cargoPid", Integer.class)
                .setParameter("cargoPid", cargoEntity.getPId())
                .getResultList();
    }

}
