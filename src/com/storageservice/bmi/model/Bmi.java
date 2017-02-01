package com.storageservice.bmi.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.storageservice.bmi.dao.StorageServiceDao;

@Entity
@Table(name = "Bmi")
@NamedQuery(name = "Bmi.findAll", query = "SELECT b FROM Bmi b")
@XmlRootElement
public class Bmi implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "idBmi")
	private int idBmi;
	@Column(name = "value")
	private double value;
	@Column(name = "status")
	private String status;
	@Column(name = "idPerson")
	private int idPerson;
	@Column(name = "risk")
	private String risk;
	@Column(name = "prime")
	private String prime;

	public double getValue() {
		return value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public int getIdBmi() {
		return idBmi;
	}

	public void setIdBmi(int idBmi) {
		this.idBmi = idBmi;
	}

	public String getPrime() {
		return prime;
	}

	public void setPrime(String prime) {
		this.prime = prime;
	}

	// Database operations
	// get the Bmi which id correspond to the given id as parameter, return a
	// Bmi
	public static Bmi getBmiById(long bmiId) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		Bmi b = em.find(Bmi.class, (int) bmiId);
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}

	// get all the Bmi present in the db, return a list of Person
	public static List<Bmi> getAll() {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		List<Bmi> list = em.createNamedQuery("Bmi.findAll", Bmi.class).getResultList();
		StorageServiceDao.instance.closeConnections(em);
		return list;
	}

	// save a new Bmi in the db
	public static Bmi saveBmi(Bmi b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}

	// delete the Bmi givean as input in the db
	public static void removeBmi(Bmi b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		b = em.merge(b);
		em.remove(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
	}

	public static Bmi updateBmi(Bmi b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		b = em.merge(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}

	// get bmi by idPerson
	public static Bmi getBmiByIdPerson(int idPerson) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		try {
			Query q = em.createQuery("SELECT b FROM Bmi b WHERE b.idPerson=:idPerson", Bmi.class);
			q.setParameter("idPerson", idPerson);

			Bmi bmi = (Bmi) q.getSingleResult();
			StorageServiceDao.instance.closeConnections(em);
			return bmi;
		} catch (Exception e) {
			System.out.println("The database doesn't contain a bmi for the person required");
			StorageServiceDao.instance.closeConnections(em);
			return null;
		}

	}

}
