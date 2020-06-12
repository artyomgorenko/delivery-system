package ru.delivery.system.dao;

import ru.delivery.system.model.entities.ProductEntity;
import ru.delivery.system.model.entities.StockentryEntity;
import ru.delivery.system.model.entities.WarehouseEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class StockentryManager {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public StockentryEntity getStockentryById(WarehouseEntity warehouse, ProductEntity product) {
        return em.createQuery("select s from StockentryEntity s " +
                "where s.warehouse=:warehouse " +
                "and s.product=:product", StockentryEntity.class)
                .setParameter("warehouse", warehouse)
                .setParameter("product", product)
                .getResultList().stream().findFirst().orElse(null);
    }

}
