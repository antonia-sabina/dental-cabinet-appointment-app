package org.proiect.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.proiect.model.Medic;
import org.proiect.model.Pacient;
import org.proiect.model.Programare;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class RepositoryProgramari implements IRepositoryProgramari{
    private EntityManager entityManager;
    private String sqlDefaultText = "SELECT o FROM Programare o";

    public RepositoryProgramari(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public RepositoryProgramari(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        this.entityManager = emf.createEntityManager();
    }
    @Override
    public Long getNumarProgramari() {
        return this.entityManager.createQuery("SELECT COUNT(p) FROM Programare o", Long.class).getSingleResult();
    }

    @Override
    public Programare getProgramareDupaData(Date data) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.dataProgramare = :dataProgramare", Programare.class).setParameter("dataProgramare", data).getSingleResult();
    }

    @Override
    public List<Programare> getProgramariDupaData(Date data) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.dataProgramare = %:dataProgramare%", Programare.class).setParameter("dataProgramare", data).getResultList();
    }

    @Override
    public Programare getProgramareDupaPacient(Pacient pacient) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.pacientProgramat = :pacient", Programare.class).setParameter("pacient" , pacient).getSingleResult();
    }

    @Override
    public List<Programare> getProgramariDupaPacient(Pacient pacient) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.pacientProgramat = :pacient", Programare.class).setParameter("pacient", pacient).getResultList();
    }

    public Programare getProgramareDupaMedic(Medic medic) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.medicProgramat = :medic", Programare.class).setParameter("medic", medic).getSingleResult();
    }

    @Override
    public List<Programare> getProgramariDupaMedic(Medic medic) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.medicProgramat = :medic", Programare.class).setParameter("medic", medic).getResultList();
    }

    @Override
    public Programare getProgramareDupaId(Integer id) {
        return this.entityManager.find(Programare.class, id);
    }

    @Override
    public void refresh(Programare programare) {
        this.entityManager.refresh(programare);
    }

    @Override
    public void remove(Programare programare) {
        Programare programarePersistenta = entityManager.find(Programare.class, programare.getIdProgramare());
        try {
            entityManager.getTransaction().begin();
            if(programarePersistenta != null)
                this.entityManager.remove(programare);
            entityManager.getTransaction().commit();
        }
        catch (Exception ex){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void add(Programare programare) {
        try{
            Programare programarePersistenta = entityManager.find(Programare.class, programare.getIdProgramare());
            entityManager.getTransaction().begin();
            if(programarePersistenta != null){
                System.out.println(">>>> to update-merge programare..." + programare);
                this.entityManager.merge(programare);
            }
            else{
                this.entityManager.persist(programare);
                System.out.println(">>>> to insert programare..." + programare);
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
    public Collection<Programare> getAll() {
        List<Programare> result = this.entityManager.createQuery(this.sqlDefaultText, Programare.class).getResultList();
        TreeSet<Programare> entitatiOrdonate = new TreeSet<Programare>((p1, p2) -> p1.getIdProgramare().compareTo(p2.getIdProgramare()));
        entitatiOrdonate.addAll(result);
        return entitatiOrdonate;
    }

    @Override
    public void removeAll() {
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Programare p").executeUpdate();
            entityManager.getTransaction().commit();
        }
        catch (Exception ex){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
