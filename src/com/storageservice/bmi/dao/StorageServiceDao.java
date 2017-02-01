package com.storageservice.bmi.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public enum StorageServiceDao {
	instance;
	private EntityManagerFactory emf;

	// entity manager init
	private StorageServiceDao() {
		if (emf != null) {
			emf.close();
		}
		emf = Persistence.createEntityManagerFactory("storage-service-jpa");
	}

	// entity manager creation
	public EntityManager createEntityManager() {
		return emf.createEntityManager();
	}

	// entity manager close
	public void closeConnections(EntityManager em) {
		em.close();
	}

	// transaction
	public EntityTransaction getTransaction(EntityManager em) {
		return em.getTransaction();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	
}