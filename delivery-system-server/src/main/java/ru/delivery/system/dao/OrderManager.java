package ru.delivery.system.dao;

import ru.delivery.system.common.enums.OrderStatus;
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

    @PersistenceContext(name = "PostgresDS")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void save(OrderEntity orderEntity) {
        em.merge(orderEntity);
        em.flush();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public OrderEntity createOrder(OrderIncoming orderIncoming) {
        TransportEntity transportEntity = transportManager.getTransportById(orderIncoming.getTransportId());
        if (transportEntity == null) {
            throw new RuntimeException("Не найдена машина с id = " + orderIncoming.getTransportId());
        }
        UserEntity userEntity = userManager.getUserById(orderIncoming.getDriverId());
        if (userEntity == null) {
            throw new RuntimeException("Не найдена водитель с id = " + orderIncoming.getTransportId());
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateDate(new Timestamp(orderIncoming.getCreateDate().getTime()));
        orderEntity.setDeparturePoint(orderIncoming.getDeparturePoint());
        orderEntity.setDestinationPoint(orderIncoming.getDestinationPoint());
        orderEntity.setStatus(OrderStatus.NEW.name());
        orderEntity.setTransport(transportEntity);
        orderEntity.setUser(userEntity);
        em.persist(orderEntity);

        // Order details
        for (OrderIncoming.Product product : orderIncoming.getProductList()) {
            ProductEntity productEntity = productManager.getProductById(product.getProductId());
            if (productEntity != null) {
                OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
                orderDetailsEntity.setProduct(productEntity);
                orderDetailsEntity.setOdId(orderEntity.getOId());
                orderDetailsEntity.setCount(product.getCount());
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
                .getSingleResult();
    }

    @TransactionAttribute
    public List<OrderEntity> getOrders() {
        return em.createQuery("select o from OrderEntity o", OrderEntity.class)
                .getResultList();
    }


}
