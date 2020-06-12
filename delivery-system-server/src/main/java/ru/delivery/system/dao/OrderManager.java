package ru.delivery.system.dao;

import ru.delivery.system.model.entities.MapMarkerEntity;
import ru.delivery.system.model.entities.MapRouteEntity;
import ru.delivery.system.model.entities.OrderEntity;
import ru.delivery.system.model.entities.OrderMapRouteEntity;
import ru.delivery.system.model.json.OrderIncoming;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

@Stateless
public class OrderManager {

    @PersistenceContext(name = "PostgresDS")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void save(OrderEntity orderEntity) {
        em.merge(orderEntity);
        em.flush();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OrderEntity createOrder(OrderIncoming orderIncoming) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateDate(new Timestamp(orderIncoming.getOrderDate().getTime()));
        em.persist(orderEntity);

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

    public OrderEntity getOrderById(Integer orderId) {
        return em.createQuery("select o from OrderEntity o where o.oId = :orderId", OrderEntity.class)
                .setParameter("orderId", orderId)
                .getSingleResult();
    }

}
