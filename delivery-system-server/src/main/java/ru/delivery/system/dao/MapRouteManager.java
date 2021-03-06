package ru.delivery.system.dao;

import ru.delivery.system.model.entities.MapRouteEntity;
import ru.delivery.system.model.entities.MapRoutePointEntity;
import ru.delivery.system.model.json.order.OrderMarkerIncoming;
import ru.delivery.system.model.json.RoutePointIncoming;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MapRouteManager {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public MapRouteEntity getMapRouteByOrderId(Integer orderId) {
        for (MapRouteEntity routeEntity : (List<MapRouteEntity>) em.createNativeQuery("select mr.* from order_ o " +
                "inner join order_map_route omr on o.o_id = omr.o_id " +
                "inner join map_route mr on omr.mr_id = mr.mr_id " +
                "where o.o_id = ?1", MapRouteEntity.class)
                .setParameter(1, orderId)
                .getResultList()) {
            return routeEntity;
        }
        return null;
    }

    @TransactionAttribute
    public void createRoutePoint(MapRouteEntity mapRouteEntity, OrderMarkerIncoming orderMarker) {
        MapRoutePointEntity mapRoutePointEntity = new MapRoutePointEntity();
        mapRoutePointEntity.setMrpMrId(mapRouteEntity.getMrId());
        mapRoutePointEntity.setMrpSerialNumber(orderMarker.getSerialNumber());
        mapRoutePointEntity.setMrpLatitude(orderMarker.getLatitude());
        mapRoutePointEntity.setMrpLongitude(orderMarker.getLongitude());
        em.persist(mapRoutePointEntity);
        em.flush();
    }

    @TransactionAttribute
    public void createRoutePoint(Integer orderId, RoutePointIncoming routePointIncoming) {
        MapRouteEntity mapRouteEntity = getMapRouteByOrderId(orderId);
        MapRoutePointEntity mapRoutePointEntity = new MapRoutePointEntity();
        mapRoutePointEntity.setMrpMrId(mapRouteEntity.getMrId());
        mapRoutePointEntity.setMapRouteEntity(mapRouteEntity);
        mapRoutePointEntity.setMapRouteEntity(mapRouteEntity);
        mapRoutePointEntity.setMrpSerialNumber(routePointIncoming.getSerialNumber());
        mapRoutePointEntity.setMrpLatitude(routePointIncoming.getGeoPoint().getLatitude());
        mapRoutePointEntity.setMrpLongitude(routePointIncoming.getGeoPoint().getLongitude());
        em.persist(mapRoutePointEntity);
        em.flush();
    }

}
