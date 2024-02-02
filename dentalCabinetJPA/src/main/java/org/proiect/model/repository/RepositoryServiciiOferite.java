package org.proiect.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.proiect.model.ServiciuOferit;

import java.util.List;
import java.util.TreeSet;

public class RepositoryServiciiOferite implements IRepositoryServiciiOferite{
    private EntityManager entityManager;
    private String sqlDefaultText = "SELECT o FROM ServiciuOferit o";
    public RepositoryServiciiOferite(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public RepositoryServiciiOferite(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public Long getNumarServiciiOferite() {
        return this.entityManager.createQuery("SELECT COUNT(p) FROM ServiciuOferit o", Long.class).getSingleResult();
    }

    @Override
    public ServiciuOferit getServiciuDupaDenumire(String denumireServiciu) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.denumireServiciu = :denumireServiciu", ServiciuOferit.class).setParameter("denumireServiciu", denumireServiciu).getSingleResult();
    }

    @Override
    public ServiciuOferit getServiciuDupaDurata(Integer durataEstimata) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.durataEstimata = :durataEstimata", ServiciuOferit.class).setParameter("durataEstimata", durataEstimata).getSingleResult();
    }

    @Override
    public List<ServiciuOferit> getServiciiDupaDurataEstimata(Integer durataEstimata) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.durataEstimata = :durataEstimata", ServiciuOferit.class).setParameter("durataEstimata", durataEstimata).getResultList();
    }

    @Override
    public ServiciuOferit getServiciuDupaId(Integer id) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.idServiciu = :id", ServiciuOferit.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public void refresh(ServiciuOferit serviciuOferit) {
        this.entityManager.refresh(serviciuOferit);
    }

    @Override
    public void remove(ServiciuOferit serviciuOferit) {
        ServiciuOferit serviciuPersistent = entityManager.find(ServiciuOferit.class, serviciuOferit.getIdServiciu());
        try {
            entityManager.getTransaction().begin();
            if(serviciuPersistent != null)
                this.entityManager.remove(serviciuOferit);
            entityManager.getTransaction().commit();
        }
        catch (Exception e){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void add(ServiciuOferit serviciuOferit) {
        try {
            ServiciuOferit serviciuPersistent = entityManager.find(ServiciuOferit.class, serviciuOferit.getIdServiciu());
            entityManager.getTransaction().begin();
            if (serviciuPersistent != null) {
                System.out.println(">>>> to update-merge serviciu...");
                this.entityManager.merge(serviciuOferit);
            } else {
                this.entityManager.persist(serviciuOferit);
                System.out.println(">>>> to insert serviciu..." + serviciuOferit);
            }
            entityManager.getTransaction().commit();
        }
        catch (Exception ex){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public List<ServiciuOferit> getAll() {
        List<ServiciuOferit> result = this.entityManager.createQuery(this.sqlDefaultText, ServiciuOferit.class).getResultList();
        TreeSet<ServiciuOferit> entitatiOrdonate = new TreeSet<ServiciuOferit>((s1, s2) -> s1.getIdServiciu().compareTo(s2.getIdServiciu()));
        entitatiOrdonate.addAll(result);
        return null;
    }

    @Override
    public void removeAll() {
        try{
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM ServiciuOferit p").executeUpdate();
            entityManager.getTransaction().commit();
        }
        catch (Exception e){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(e.getMessage());
        }

    }
}
