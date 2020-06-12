package ru.delivery.system.dao;

import ru.delivery.system.model.entities.MapMarkerEntity;
import ru.delivery.system.model.json.MapMarkerIncoming;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MapMarkerManager {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createMarker(MapMarkerIncoming mapMarker) {
        MapMarkerEntity mapMarkerEntity = new MapMarkerEntity();
        mapMarkerEntity.setLatitude(mapMarker.getLatitude());
        mapMarkerEntity.setLongitude(mapMarker.getLongitude());

        em.persist(mapMarkerEntity);
        em.flush();
    }

    public List<MapMarkerEntity> getMapMarkers() {
        return em.createQuery("select a from MapMarkerEntity a", MapMarkerEntity.class)
                .getResultList();
    }

}
