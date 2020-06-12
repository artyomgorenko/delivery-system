package ru.delivery.system.dao;

import ru.delivery.system.model.entities.WarehouseEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class WarehouseManager {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public List<WarehouseEntity> getAllWarehouses() {
        return em.createQuery("select w from WarehouseEntity w", WarehouseEntity.class).getResultList();
    }

    @TransactionAttribute
    public void save(WarehouseEntity warehouseEntity) {
        em.merge(warehouseEntity);
        em.flush();
    }
}
