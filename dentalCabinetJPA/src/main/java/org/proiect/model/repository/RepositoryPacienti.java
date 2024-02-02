package org.proiect.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.proiect.model.Pacient;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class RepositoryPacienti implements IRepositoryPacienti {

    private EntityManager entityManager;
    private String sqlDefaultText = "SELECT o FROM Pacient o";

    public RepositoryPacienti(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public RepositoryPacienti(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        this.entityManager = emf.createEntityManager();
    }
    @Override
    public Long getNumarPacienti() {
        return this.entityManager.createQuery("SELECT COUNT(p) FROM Pacient p", Long.class).getSingleResult();
    }

    @Override
    public Pacient getPacientDupaNume(String nume) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.numePacient = :numePacient", Pacient.class).setParameter("numePacient", nume).getSingleResult();
    }

    @Override
    public Pacient getPacientDupaPrenume(String prenume) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.prenumePacient = :prenumePacient", Pacient.class).setParameter("prenumePacient", prenume).getSingleResult();
    }

    @Override
    public Pacient getPacientDupaNumeSiPrenume(String nume, String prenume) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.nume = :numePacient AND o.prenume = :prenumePacient", Pacient.class).setParameter("numePacient", nume).setParameter("prenumePacient", prenume).getSingleResult();
    }

    @Override
    public List<Pacient> getPacientiDupaNume(String nume) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.numePacient LIKE %:numePacient%", Pacient.class).setParameter("numePacient", nume).getResultList();
    }

    @Override
    public List<Pacient> getPacientiDupaPrenume(String prenume) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.prenumePacient LIKE %:prenumePacient%", Pacient.class).setParameter("prenumePacient", prenume).getResultList();
    }

    @Override
    public List<Pacient> getPacientiDupaNumeSiPrenume(String nume, String prenume) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.numePacient LIKE %:numePacient% AND o.prenumePacient LIKE %:prenumePacient%", Pacient.class).setParameter("numePacient", nume).setParameter("prenumePacient", prenume).getResultList();
    }

    @Override
    public Pacient getPacientDupaCnp(Long cnp) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.cnpPacient = :cnpPacient", Pacient.class).setParameter("cnpPacient", cnp).getSingleResult();
    }

    @Override
    public Pacient getPacientDupaNumarTelefon(String numarTelefon) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.numarTelefonPacient = :numarTelefonPacient", Pacient.class).setParameter("numarTelefonPacient", numarTelefon).getSingleResult();
    }

    @Override
    public Pacient getPacientDupaId(Integer id) {
        Pacient p = this.entityManager.find(Pacient.class, id);
        return p;
    }

    @Override
    public void refresh(Pacient pacient) {
        this.entityManager.refresh(pacient);
    }

    @Override
    public void remove(Pacient pacient) {
        Pacient pacientPersistent = entityManager.find(Pacient.class, pacient.getIdPacient());
        try {
            entityManager.getTransaction().begin();
            if(pacientPersistent != null)
                this.entityManager.remove(pacient);
            entityManager.getTransaction().commit();
        }
        catch (Exception ex){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }

    }

    @Override
    public void add(Pacient pacient) {
        try{
            Pacient pacientPersistent = entityManager.find(Pacient.class, pacient.getIdPacient());
            entityManager.getTransaction().begin();
            if(pacientPersistent != null){
                System.out.println(">>>> to update-merge pacient..." + pacient);
                this.entityManager.merge(pacient);
            }
            else{
                this.entityManager.persist(pacient);
                System.out.println(">>>> to insert pacient..." + pacient);
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
    public Collection<Pacient> getAll() {
        List<Pacient> result = this.entityManager.createQuery(this.sqlDefaultText, Pacient.class).getResultList();
        TreeSet<Pacient> entitatiOrdonate = new TreeSet<Pacient>((p1, p2) -> p1.getIdPacient().compareTo(p2.getIdPacient()));
        entitatiOrdonate.addAll(result);
        return entitatiOrdonate;
    }

    @Override
    public void removeAll() {
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Pacient p").executeUpdate();
            entityManager.getTransaction().commit();
        }
        catch (Exception ex){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }
}