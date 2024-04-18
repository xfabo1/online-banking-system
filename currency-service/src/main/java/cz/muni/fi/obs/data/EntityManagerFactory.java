package cz.muni.fi.obs.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class EntityManagerFactory {

    jakarta.persistence.EntityManagerFactory emf = Persistence.createEntityManagerFactory("cz.muni.fi.obs.data");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
