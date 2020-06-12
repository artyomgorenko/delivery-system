package ru.delivery.system.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class JobManager {

    @PersistenceContext(unitName = "PostgresDS")
    private EntityManager em;

    public List<String> getJobNames() {
        return em.createNativeQuery("select JOB_NAME from tool_rental.JOBS").getResultList();
    }


}
