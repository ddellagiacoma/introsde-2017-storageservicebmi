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
@Table(name = "BmiHistory")
@NamedQuery(name = "BmiHistory.findAll", query = "SELECT b FROM BmiHistory b")
@XmlRootElement
public class BmiHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "idBmiHistory")
	private int idBmiHistory;
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
	private String date;

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

	public int getIdBmiHistory() {
		return idBmiHistory;
	}

	public void setIdBmiHistory(int idBmiHistory) {
		this.idBmiHistory = idBmiHistory;
	}

	public String getPrime() {
		return prime;
	}

	public void setPrime(String prime) {
		this.prime = prime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	// Database operations
	// get the BmiHistory which id correspond to the given id as parameter,
	// return a
	// BmiHistory
	public static BmiHistory getBmiHistoryById(long bmiHistoryId) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		BmiHistory b = em.find(BmiHistory.class, (int) bmiHistoryId);
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}

	// get all the BmiHistory present in the db, return a list of Person
	public static List<BmiHistory> getAll() {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		List<BmiHistory> list = em.createNamedQuery("BmiHistory.findAll", BmiHistory.class).getResultList();
		StorageServiceDao.instance.closeConnections(em);
		return list;
	}

	// save a new BmiHistory in the db
	public static BmiHistory saveBmiHistory(BmiHistory b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
		return b;
	}

	public static BmiHistory getBmiHistoryByDate(String date) {

		EntityManager em = StorageServiceDao.instance.createEntityManager();
		try {
			Query q = em.createQuery("SELECT b FROM BmiHistory b WHERE b.date>:date order by b.date asc",
					BmiHistory.class);
			q.setParameter("date", date);

			List<BmiHistory> histories = q.getResultList();
			BmiHistory history = histories.get(0);
			StorageServiceDao.instance.closeConnections(em);
			return history;
		} catch (Exception e) {
			System.out.println("The database doesn't contain a BmiHistory with the date required");

			StorageServiceDao.instance.closeConnections(em);
			return null;
		}

	}

	// delete the BmiHistory givean as input in the db
	public static void removeBmiHistory(BmiHistory b) {
		EntityManager em = StorageServiceDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		b = em.merge(b);
		em.remove(b);
		tx.commit();
		StorageServiceDao.instance.closeConnections(em);
	}

}
