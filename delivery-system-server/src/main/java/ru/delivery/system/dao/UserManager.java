package ru.delivery.system.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserManager {
    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;


}
