package ru.delivery.system.dao;

import ru.delivery.system.model.entities.ProductEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProductManager {

    @PersistenceContext(name = "PostgresDS")
    private EntityManager em;

    public ProductEntity getProductById(Integer productId) {
        return em.createQuery("select o from ProductEntity o where o.id = :productId", ProductEntity.class)
                .setParameter("productId", productId)
                .getSingleResult();
    }


    public List<ProductEntity> getProductList() {
        return em.createQuery("select p from ProductEntity p", ProductEntity.class).getResultList();
    }

    @TransactionAttribute
    public ProductEntity createProduct() {
        return null;
    }
}
