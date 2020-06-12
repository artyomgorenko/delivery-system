package ru.delivery.system.dao;

import ru.delivery.system.model.entities.UsersEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserManager {
    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public UsersEntity findUser(String username, String password) {
        for (UsersEntity user : em.createQuery("select u from UsersEntity u " +
                "where u.username = :userName " +
                "and u.password = :password", UsersEntity.class)
                .setParameter("userName", username)
                .setParameter("password", password)
                .getResultList()) {
            return user;
        }
        return null;
    }
}
