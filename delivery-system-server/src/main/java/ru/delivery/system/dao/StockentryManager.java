package ru.delivery.system.dao;

import ru.delivery.system.model.entities.ProductEntity;
import ru.delivery.system.model.entities.StockentryEntity;
import ru.delivery.system.model.entities.WarehouseEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
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


    /**
     * Списание определенного количества товара с заданного склада
     */
    @TransactionAttribute
    public void writeoffProduct(Integer productId, Integer warehouseId, int writeoffCount) {
        em.createNativeQuery("update stockentry set " +
                "s_quantity = s_quantity - ?1 " +
                "where sp_id = ?2 " +
                "and sw_id = ?3")
        .setParameter(1, writeoffCount)
        .setParameter(2, productId)
        .setParameter(3, warehouseId);
    }


}
