package ru.delivery.system.dao;

import ru.delivery.system.model.entities.TransportEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TransportManager {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public TransportEntity getTransportById(Integer transportId) {
        return em.createQuery("select t from TransportEntity t " +
                "where t.id=:transportId", TransportEntity.class)
                .setParameter("transportId", transportId)
                .getSingleResult();
    }

}
