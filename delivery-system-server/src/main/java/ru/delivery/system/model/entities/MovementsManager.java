package ru.delivery.system.model.entities;

import ru.delivery.system.dao.ProductManager;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Stateless
public class MovementsManager {

    @PersistenceContext(name = "PostgresDS")
    private EntityManager em;

    @EJB
    private ProductManager productManager;

    /**
     * Изменяет статус товара и сохраняет факт изменения в таблице MOVEMENTS
     */
    @TransactionAttribute
    public void makeMovement(CargoEntity cargoEntity, String newStatus) {
        String prevStatus = cargoEntity.getStatus();
        cargoEntity.setStatus(newStatus);
        em.merge(cargoEntity);

        MovementsEntity movementsEntity = new MovementsEntity();
        movementsEntity.setNewStatus(newStatus);
        movementsEntity.setPrevStatus(prevStatus);
        movementsEntity.setProduct(cargoEntity);
        movementsEntity.setMDate(newTimestamp());
        em.persist(movementsEntity);
        em.flush();
    }

    @TransactionAttribute
    public List<MovementsEntity> getCargoMovements(CargoEntity cargoEntity) {
        return em.createQuery("select m from MovementsEntity m " +
                "where m.product.id=:cargoId", MovementsEntity.class)
                .setParameter("cargoId", cargoEntity.getPId())
                .getResultList();
    }

    public static Timestamp newTimestamp() {
        return new Timestamp(new Date().getTime());
    }
}
