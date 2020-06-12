package ru.delivery.system.dao;

import ru.delivery.system.model.entities.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
