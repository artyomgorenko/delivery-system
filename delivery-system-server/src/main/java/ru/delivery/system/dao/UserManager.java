package ru.delivery.system.dao;

import ru.delivery.system.model.entities.UserEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserManager {
    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public UserEntity findUser(String username, String password) {
        for (UserEntity user : em.createQuery("select u from UserEntity u " +
                "where u.username = :userName " +
                "and u.password = :password", UserEntity.class)
                .setParameter("userName", username)
                .setParameter("password", password)
                .getResultList()) {
            return user;
        }
        return null;
    }

    public UserEntity getUserById(Integer userId) {
        return em.createQuery("select u from UserEntity u " +
                "where u.id=:userId", UserEntity.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public List<UserEntity> getAllUsers() {
        return em.createQuery("select u from UserEntity u", UserEntity.class).getResultList();
    }

    @TransactionAttribute
    public void save(UserEntity userEntity) {
        em.merge(userEntity);
        em.flush();
    }
}
