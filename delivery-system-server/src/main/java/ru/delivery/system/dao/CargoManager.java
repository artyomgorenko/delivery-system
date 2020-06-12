package ru.delivery.system.dao;

import ru.delivery.system.common.enums.ProductStatus;
import ru.delivery.system.model.entities.CargoEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CargoManager {

    @PersistenceContext(name = "PostgresDS")
    private EntityManager em;

    /**
     *  Возврщает список продуктов, которые есть на складе
     */
    public List<CargoEntity> getCargosForReserve(Integer cargoId, Integer count) {
        return em.createNativeQuery("select * " +
                "from products " +
                "where status = ?1 " +
                "and pp_id = ?2 " +
                "limit ?3", CargoEntity.class)
                .setParameter(1, ProductStatus.ACTIVE)
                .setParameter(2, cargoId)
                .setParameter(3, count)
                .getResultList();
    }

    public List<CargoEntity> getCargoList() {
        return em.createNativeQuery("select * " +
                "from products ", CargoEntity.class)
                .getResultList();
    }
}
