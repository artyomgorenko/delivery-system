package ru.delivery.system.dao;

import ru.delivery.system.model.entities.HermesMapMarker;
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
        HermesMapMarker hermesMapMarker = new HermesMapMarker();
        hermesMapMarker.setLatitude(mapMarker.getLatitude());
        hermesMapMarker.setLongitude(mapMarker.getLongitude());

        em.persist(hermesMapMarker);
        em.flush();
    }

    public List<HermesMapMarker> getMapMarkers() {
        return em.createQuery("select a from HermesMapMarker a", HermesMapMarker.class)
                .getResultList();
    }

}
