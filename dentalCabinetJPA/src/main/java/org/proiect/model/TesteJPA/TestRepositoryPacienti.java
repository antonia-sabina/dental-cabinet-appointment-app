package org.proiect.model.TesteJPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.proiect.model.Pacient;
import org.proiect.model.repository.IRepositoryPacienti;
import org.proiect.model.repository.RepositoryPacienti;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestRepositoryPacienti {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        EntityManager em = emf.createEntityManager();

        Query queryDeleteProgramari = em.createQuery("DELETE FROM Programare p");
        Query queryDeleteInterventii = em.createQuery("DELETE FROM Interventie i");
        Query queryDeleteEchipamente = em.createQuery("DELETE FROM Echipament e");
        Query queryDeleteConsultatii = em.createQuery("DELETE FROM Consultatie c");
        Query queryDeleteMedici = em.createQuery("DELETE FROM Medic m");
        Query queryDeletePacienti = em.createQuery("DELETE FROM Pacient p");
        Query queryDeleteAsistenti = em.createQuery("DELETE FROM Asistent a");
        Query queryDeleteMedicamente = em.createQuery("DELETE FROM Medicament m");
        Query queryDeleteTratamente = em.createQuery("DELETE FROM Tratament t");
        Query queryDeleteInterventiiPrestate = em.createQuery("DELETE FROM InterventiePrestata i");
        Query queryDeleteConsultatiiPrestate = em.createQuery("DELETE FROM ConsultatiePrestata c");

        IRepositoryPacienti repositoryPacienti = new RepositoryPacienti();

        em.getTransaction().begin();
        queryDeleteConsultatiiPrestate.executeUpdate();
        queryDeleteInterventiiPrestate.executeUpdate();
        queryDeleteProgramari.executeUpdate();
        queryDeletePacienti.executeUpdate();
        queryDeleteAsistenti.executeUpdate();
        queryDeleteMedici.executeUpdate();
        queryDeleteInterventii.executeUpdate();
        queryDeleteConsultatii.executeUpdate();
        queryDeleteEchipamente.executeUpdate();
        queryDeleteTratamente.executeUpdate();
        queryDeleteMedicamente.executeUpdate();
        em.getTransaction().commit();
        em.clear();

        List<Pacient> pacienti = new ArrayList<Pacient>();
        pacienti.add(new Pacient(1, "Popescu", "Ion", "1111111111111", "0711111111"));
        pacienti.add(new Pacient(2, "Ionescu", "Vasile", "1111111111112", "0711111112"));
        pacienti.add(new Pacient(3, "Marinescu", "Ana", "1111111111113", "0711111113"));
        pacienti.add(new Pacient(4, "Constantinescu", "Andreea", "1111111111114", "0711111114"));

        repositoryPacienti.add(pacienti.get(0));
        repositoryPacienti.add(pacienti.get(1));
        repositoryPacienti.add(pacienti.get(2));
        repositoryPacienti.add(pacienti.get(3));

        Collection<Pacient> lstPacientiPersistenti = repositoryPacienti.getAll();
        System.out.println("lista pacienti persistenti/salvati in baza de date");
        for(Pacient p: lstPacientiPersistenti)
            System.out.println("id: " + p.getIdPacient() + ", nume: " + p.getNumePacient() + ", prenume: " + p.getPrenumePacient() + ", cnp: " + p.getCnpPacient() + ", numarTelefon: " + p.getNumarTelefonPacient());

        Pacient p2 = repositoryPacienti.getPacientDupaId(2);
        if(p2 != null)
            p2.setPrenumePacient("Andrei");

        Pacient p3 = repositoryPacienti.getPacientDupaId(3);
        if(p3 != null)
            repositoryPacienti.remove(p3);

        System.out.println("lista finala pacienti persistenti/salvati in baza de date");
        repositoryPacienti.getAll().stream().forEach(p -> System.out.println("id: " + p.getIdPacient() + ", nume: " + p.getNumePacient() + ", prenume: " + p.getPrenumePacient() + ", cnp: " + p.getCnpPacient() + ", numarTelefon: " + p.getNumarTelefonPacient()));

        repositoryPacienti.removeAll();
        System.out.println("verificare stergere pacienti");
        repositoryPacienti.getAll().stream().forEach(p -> System.out.println("id: " + p.getIdPacient() + ", nume: " + p.getNumePacient() + ", prenume: " + p.getPrenumePacient() + ", cnp: " + p.getCnpPacient() + ", numarTelefon: " + p.getNumarTelefonPacient()));
    }
}
