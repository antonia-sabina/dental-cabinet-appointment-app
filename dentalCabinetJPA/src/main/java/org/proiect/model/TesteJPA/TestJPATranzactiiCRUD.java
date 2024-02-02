package org.proiect.model.TesteJPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.proiect.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestJPATranzactiiCRUD {
    public static void main(String[] args) {
        entityManagerCRUD_test();
    }

    static void checkEntityModel(EntityManagerFactory emf){
        System.out.println("--------------------");
        emf.getProperties().forEach((p, v) -> System.out.println(p.toString() + " = " + v.toString()));
        System.out.println("--------------------");
        emf.getMetamodel().getEntities().forEach(e -> System.out.println(e.getName()));
        System.out.println("--------------------");
    }

    static void entityManagerCRUD_test() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        EntityManager em = emf.createEntityManager();
        checkEntityModel(em.getEntityManagerFactory());

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

        Query queryProgramari = em.createQuery("SELECT p FROM Programare p", Programare.class);
        Query queryMedic = em.createQuery("SELECT m from Medic m WHERE m.idPersonal = 1", Medic.class);
        Query queryEchipament = em.createQuery("SELECT e FROM Echipament e WHERE e.idEchipament = 1", Echipament.class);
        Query queryConsultatie = em.createQuery("SELECT c FROM Consultatie c WHERE c.idServiciu = 1", Consultatie.class);
        Query queryInterventie = em.createQuery("SELECT i FROM Interventie i WHERE i.idServiciu = 5", Interventie.class);
        Query queryMedicament = em.createQuery("SELECT m FROM Medicament m WHERE m.idMedicament = 1", Medicament.class);
        Query queryPacient = em.createQuery("SELECT p FROM Pacient p where p.idPacient = 1", Pacient.class);



        //stergere date existente
        System.out.println("DELETE date");

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

        System.out.println("start tranzactie: CREATE-UPDATE-DELETE -------------");
        em.getTransaction().begin();
        //populare Echipamente
        System.out.println("creare echipamente");
        //ortodontie
        List<Echipament> echipamente = new ArrayList<Echipament>();
        echipamente.add(new Echipament(1, TipEchipament.APARAT_ORTODONTIC, "corecteaza alinierea dintilor"));
        echipamente.add(new Echipament(2, TipEchipament.GUTIERA, "cprotejeaza si ajusteaza dintii in somn"));
        //chirurgie
        echipamente.add(new Echipament(3, TipEchipament.PENSA, "instrument pentru manipularea precisa a materialelor dentar"));
        echipamente.add(new Echipament(4, TipEchipament.BISTURIU, "instrument chirurgical pentru interventii delicate"));
        echipamente.add(new Echipament(5, TipEchipament.DIGA, "izoleaza zona de lucru in timpul procedurilor dentare"));
        echipamente.add(new Echipament(6, TipEchipament.TUB_DRENAJ, " asigura drenajul adecvat al fluidelorr"));
        echipamente.add(new Echipament(7, TipEchipament.OGLINDA, "permite vizualizarea zonelor dificil accesibile in cavitatea orala"));
        //endodontie
        echipamente.add(new Echipament(8, TipEchipament.POMPA_DRENARE, "elimina eficient fluidele excedentare"));
        echipamente.add(new Echipament(9, TipEchipament.FREZA, "instrument pentru prelucrarea precisa a dintilor"));
        echipamente.add(new Echipament(10, TipEchipament.PLOMBA_DEFINITIVA, "restaureaza si protejeaza dintii afectati"));
        echipamente.add(new Echipament(11, TipEchipament.MICROSCOP, "furnizeaza detaliu si vizibilitate crescuta"));
        //protetica
        echipamente.add(new Echipament(12, TipEchipament.SCANNER_3D_AMPRENTA, "obtine amprente digitale precise"));
        echipamente.add(new Echipament(13, TipEchipament.LINGURA_AMPRENTA, "preia detaliile anatomic ale arcadei dentare"));
        echipamente.add(new Echipament(14, TipEchipament.CIMENT, "fixeaza si sigileaza materiale dentare"));
        echipamente.add(new Echipament(15, TipEchipament.CIMENT, "echipament de test"));
        for(Echipament echipament: echipamente)
            em.persist(echipament);

        //populare ServiciiOferite
        System.out.println("creare servicii oferite");
        //consultatii
        List<Consultatie> consultatii = new ArrayList<Consultatie>();
        consultatii.add(new Consultatie(1, "consult ORTODONT", 10, 30.0, "consultatie de rutina facuta de ortodont", SpecializareMedic.ORTODONTIE));
        consultatii.add(new Consultatie(2, "consult CHIRURGIE MAXILO-FACIALA", 20, 50.0, "consultatie de rutina facuta de chirurg", SpecializareMedic.CHIRURGIE_MAXILO_FACIALA));
        consultatii.add(new Consultatie(3, "consult ENDODONTIE", 15, 30.0, "consultatie de rutina facuta de un endodont", SpecializareMedic.ENDODONTIE));
        consultatii.add(new Consultatie(4, "consultatie PROTETICA", 30, 50.0, "consultatie de rutina facuta de un PROTETICIAN", SpecializareMedic.PROTETICA));
        for(Consultatie consultatie: consultatii)
            em.persist(consultatie);

        //interventii
        List<Interventie> interventii = new ArrayList<Interventie>();
        interventii.add(new Interventie(5, "interventie ortodont corectare malocluzie", 60, 100.0, TipInterventie.CORECTAREA_MALOCLUZIEI, List.of(echipamente.get(0), echipamente.get(1))));
        interventii.add(new Interventie(6, "interventie ortodont pentru tratarea bruxismului", 60, 100.0, TipInterventie.TRATAREA_BRUXISMULUI, List.of(echipamente.get(0), echipamente.get(1))));
        interventii.add(new Interventie(7, "interventie chirurg sinus lift", 60, 100.0, TipInterventie.SINUS_LIFT, List.of(echipamente.get(2), echipamente.get(3), echipamente.get(4), echipamente.get(5), echipamente.get(6))));
        interventii.add(new Interventie(8, "interventie chirurg dilatare bolta platinata", 60, 100.0, TipInterventie.DILATARE_BOLTA_PALATINA, List.of(echipamente.get(2), echipamente.get(3), echipamente.get(4), echipamente.get(5), echipamente.get(6))));
        interventii.add(new Interventie(9, "interventie endodont eliminare infectii canal", 60, 100.0, TipInterventie.ELIMINARE_INFECTII_CANAL, List.of(echipamente.get(7), echipamente.get(8), echipamente.get(9), echipamente.get(10))));
        interventii.add(new Interventie(10, "interventie endodont eliminare nerv compromis", 60, 100.0, TipInterventie.ELIMINARE_NERV_COMPROMIS, List.of(echipamente.get(7), echipamente.get(8), echipamente.get(9), echipamente.get(10))));
        interventii.add(new Interventie(11, "interventie protetician montare fatete", 60, 100.0, TipInterventie.MONTARE_FATETE, List.of(echipamente.get(11), echipamente.get(12), echipamente.get(13))));
        interventii.add(new Interventie(12, "interventie protetician montare proteze", 60, 100.0, TipInterventie.MONTARE_PROTEZE, List.of(echipamente.get(11), echipamente.get(12), echipamente.get(13))));
        for(Interventie interventie: interventii)
            em.persist(interventie);

        //populare Pacienti
        System.out.println("creare pacienti");
        List<Pacient> pacienti = new ArrayList<Pacient>();
        pacienti.add(new Pacient(1, "Popescu", "Ion", "1111111111111", "0711111111"));
        pacienti.add(new Pacient(2, "Ionescu", "Vasile", "1111111111112", "0711111112"));
        pacienti.add(new Pacient(3, "Marinescu", "Ana", "1111111111113", "0711111113"));
        pacienti.add(new Pacient(4, "Constantinescu", "Andreea", "1111111111114", "0711111114"));
        pacienti.add(new Pacient(5, "Ispir", "Matei", "1111111111115", "0711111115"));
        pacienti.add(new Pacient(6, "Gheorghe", "Marin", "1111111111116", "0711111116"));
        for(Pacient pacient: pacienti)
            em.persist(pacient);

        //populare Personal
        System.out.println("creare personal");
        //populare Medici
        List<Medic> medici = new ArrayList<Medic>();
        List<Asistent> asistenti1 = new ArrayList<Asistent>();
        medici.add(new Medic(1, "Popescu", "Andrei", "1111111111121", "0711111121", SpecializareMedic.ORTODONTIE, asistenti1, List.of(TipInterventie.CORECTAREA_MALOCLUZIEI, TipInterventie.TRATAREA_BRUXISMULUI)));
        List<Asistent> asistenti2 = new ArrayList<Asistent>();
        medici.add(new Medic(2, "Dumitrescu", "Elena", "1111111111122", "0711111122", SpecializareMedic.CHIRURGIE_MAXILO_FACIALA, asistenti2, List.of(TipInterventie.SINUS_LIFT, TipInterventie.DILATARE_BOLTA_PALATINA)));
        List<Asistent> asistenti3 = new ArrayList<Asistent>();
        medici.add(new Medic(3, "Ionescu", "Radu", "1111111111123", "0711111123", SpecializareMedic.ENDODONTIE, asistenti3, List.of(TipInterventie.ELIMINARE_INFECTII_CANAL, TipInterventie.ELIMINARE_NERV_COMPROMIS)));
        List<Asistent> asistenti4 = new ArrayList<Asistent>();
        medici.add(new Medic(4, "Istrate", "Ana", "1111111111124", "0711111124", SpecializareMedic.PROTETICA, asistenti4, List.of(TipInterventie.MONTARE_FATETE, TipInterventie.MONTARE_PROTEZE)));
        for(Medic medic: medici)
            em.persist(medic);

        //populare Asistenti
        System.out.println("creare asistenti");
        List<Asistent> asistenti = new ArrayList<Asistent>();
        asistenti.add(new Asistent(5, "Radulescu", "Ioana", "1111111111131", "0711111131", medici.get(0)));
        asistenti.add(new Asistent(6, "Dumitrache", "Adrian", "1111111111132", "0711111132", medici.get(0)));
        asistenti1 = List.of(asistenti.get(0), asistenti.get(1));
        asistenti.add(new Asistent(7, "Dragomir", "Ion", "1111111111133", "0711111133", medici.get(1)));
        asistenti.add(new Asistent(8, "Vasilescu", "Ana", "1111111111134", "0711111134", medici.get(1)));
        asistenti2 = List.of(asistenti.get(2), asistenti.get(3));
        asistenti.add(new Asistent(9, "Stoica", "Victor", "1111111111135", "0711111135", medici.get(2)));
        asistenti.add(new Asistent(10, "Istrate", "Roxana", "1111111111136", "0711111136", medici.get(2)));
        asistenti3 = List.of(asistenti.get(4), asistenti.get(5));
        asistenti.add(new Asistent(11, "Popa", "Marius", "1111111111137", "0711111137", medici.get(3)));
        asistenti.add(new Asistent(12, "Mihai", "Alina", "1111111111138", "0711111138", medici.get(3)));
        asistenti4 = List.of(asistenti.get(6), asistenti.get(7));
        for(Asistent asistent: asistenti)
            em.persist(asistent);


        //populare Programari
        System.out.println("creare programari");
        List<Programare> programari = new ArrayList<Programare>();
        programari.add(new Programare(1, new Date(2024 - 1900, Calendar.JANUARY, 23, 10, 0), pacienti.get(0), medici.get(0), interventii.get(0), StatusProgramare.URMEAZA));
        programari.add(new Programare(2, new Date(2023 - 1900, Calendar.FEBRUARY, 8, 10, 15), pacienti.get(1), medici.get(1), interventii.get(2), StatusProgramare.FINALIZATA));
        programari.get(0).setStatusProgramare();
        programari.add(new Programare(3, new Date(2023 - 1900, Calendar.DECEMBER, 31, 12, 30), pacienti.get(2), medici.get(2), consultatii.get(2), StatusProgramare.FINALIZATA));
        programari.add(new Programare(4, new Date(2024 - 1900, Calendar.JANUARY, 28, 11, 0), pacienti.get(3), medici.get(3), interventii.get(7), StatusProgramare.URMEAZA));
        programari.get(3).setStatusProgramare();
        programari.add(new Programare(5, new Date(2023 - 1900, Calendar.MARCH, 29, 12, 45), pacienti.get(0), medici.get(2), interventii.get(4), StatusProgramare.FINALIZATA));
        programari.add(new Programare(6, new Date(2023 - 1900, Calendar.APRIL, 17, 10, 15), pacienti.get(3), medici.get(1), consultatii.get(2), StatusProgramare.FINALIZATA));
        programari.add(new Programare(7, new Date(2023 - 1900, Calendar.JULY, 6, 16, 30), pacienti.get(2), medici.get(0), interventii.get(1), StatusProgramare.FINALIZATA));
        programari.add(new Programare(8, new Date(2024 -1900, Calendar.MARCH, 13, 10, 20), pacienti.get(1), medici.get(2), interventii.get(4), StatusProgramare.URMEAZA));
        programari.add(new Programare(9, new Date(2024 - 1900, Calendar.FEBRUARY, 12, 15, 30), pacienti.get(4), medici.get(1), consultatii.get(1), StatusProgramare.URMEAZA));
        for(Programare programare: programari) {
            em.persist(programare);
        }
        //populare Medicament
        System.out.println("creare medicamente");
        List<Medicament> medicamente = new ArrayList<Medicament>();
        medicamente.add(new Medicament(1, "ibuprofen", FormaMedicament.COMPRIMAT, ModAdministrare.ORAL, 10.0));
        medicamente.add(new Medicament(2, "paracetamol", FormaMedicament.COMPRIMAT, ModAdministrare.ORAL, 15.0));
        medicamente.add(new Medicament(3, "Lidocaina", FormaMedicament.INJECTABIL, ModAdministrare.INJECTIE, 25.0));
        medicamente.add(new Medicament(4, "Amoxicilina", FormaMedicament.COMPRIMAT, ModAdministrare.ORAL, 18.0));
        medicamente.add(new Medicament(5, "Ketoprofen", FormaMedicament.SIROP, ModAdministrare.ORAL, 12.0));
        medicamente.add(new Medicament(6, "Morfina", FormaMedicament.INJECTABIL, ModAdministrare.INJECTIE, 30.0));
        medicamente.add(new Medicament(7, "Gel Dentar", FormaMedicament.GEL, ModAdministrare.ORAL, 20.0));
        medicamente.add(new Medicament(8, "Aspirina", FormaMedicament.COMPRIMAT, ModAdministrare.ORAL, 8.0));
        medicamente.add(new Medicament(9, "medicament test", FormaMedicament.INJECTABIL, ModAdministrare.INJECTIE, 10.0));
        for(Medicament medicament: medicamente)
            em.persist(medicament);

        //populare Tratamente
        System.out.println("creare tratamente");
        List<Tratament> tratamente = new ArrayList<Tratament>();
        tratamente.add(new Tratament(1, 7.0, List.of(medicamente.get(0), medicamente.get(1))));
        tratamente.add(new Tratament(2, 10.0, List.of(medicamente.get(2), medicamente.get(3))));
        tratamente.add(new Tratament(3, 3.0, List.of(medicamente.get(4), medicamente.get(5))));
        tratamente.add(new Tratament(4, 5.0, List.of(medicamente.get(6), medicamente.get(7))));
        for(Tratament tratament: tratamente)
            em.persist(tratament);

        //populare ServiciiPrestate
        System.out.println("creare servicii prestate");
        //consultatii prestate
        List<ConsultatiePrestata> consultatiiPrestate = new ArrayList<ConsultatiePrestata>();
        consultatiiPrestate.add(new ConsultatiePrestata(1, programari.get(2), "fara observatii","diagnostic 1", tratamente.get(0)));
        consultatiiPrestate.add(new ConsultatiePrestata(3, programari.get(5), "fara observatii","diagnostic 2", tratamente.get(1)));
        for(ConsultatiePrestata consultatiePrestata: consultatiiPrestate)
            em.persist(consultatiePrestata);

        //interventii prestate
        List<InterventiePrestata> interventiiPrestate = new ArrayList<InterventiePrestata>();
        interventiiPrestate.add(new InterventiePrestata(2, programari.get(4), "fara observatii", asistenti.get(4), "nu au existat complicatii"));
        interventiiPrestate.add(new InterventiePrestata(4, programari.get(6), "fara observatii", asistenti.get(1), "nu au existat complicatii"));
        for(InterventiePrestata interventiePrestata: interventiiPrestate)
            em.persist(interventiePrestata);

        //UPDATE
        System.out.println("update");
        Medic medicTest = (Medic) queryMedic.getSingleResult();
        medicTest.setNumePersonal(medicTest.getNumePersonal() + " - MODIFICAT");
        Echipament echipamentTest = (Echipament) queryEchipament.getSingleResult();
        echipamentTest.setDescriereEchipament(echipamentTest.getDescriereEchipament() + " - MODIFICAT");
        Consultatie consultatieTest  = (Consultatie) queryConsultatie.getSingleResult();
        consultatieTest.setDenumireServiciu(consultatieTest.getDenumireServiciu() + " - MODIFICAT");
        Interventie interventieTest = (Interventie) queryInterventie.getSingleResult();
        interventieTest.setDenumireServiciu(interventieTest.getDenumireServiciu() + " - MODIFICAT");
        Medicament medicamentTest = (Medicament) queryMedicament.getSingleResult();
        medicamentTest.setDenumireMedicament(medicamentTest.getDenumireMedicament() + " - MODIFICAT");
        Pacient pacientTest = (Pacient) queryPacient.getSingleResult();
        pacientTest.setNumePacient(pacientTest.getNumePacient() + " - MODIFICAT");

        //UPDATE MERGE
        System.out.println("update merge");
        medicTest = new Medic(4, "Istrate - MERGED", "Ana", "1111111111124", "0711111124", SpecializareMedic.PROTETICA, asistenti4, List.of(TipInterventie.MONTARE_FATETE, TipInterventie.MONTARE_PROTEZE));
        em.merge(medicTest);
        echipamentTest = new Echipament(2, TipEchipament.GUTIERA, "protejeaza si ajusteaza dintii in somn - MERGED");
        em.merge(echipamentTest);
        consultatieTest = new Consultatie(3, "consult ENDODONTIE - MERGED", 15, 30.0, "consultatie de rutina facuta de un endodont", SpecializareMedic.ENDODONTIE);
        em.merge(consultatieTest);
        interventieTest = new Interventie(6, "interventie ortodont pentru tratarea bruxismului - MERGED", 60, 100.0, TipInterventie.TRATAREA_BRUXISMULUI, List.of(echipamente.get(0), echipamente.get(1)));
        em.merge(interventieTest);
        medicamentTest = new Medicament(2, "paracetamol - MERGED", FormaMedicament.COMPRIMAT, ModAdministrare.ORAL, 15.0);
        em.merge(medicamentTest);
        pacientTest = new Pacient(2, "Ionescu - MERGED", "Vasile", "1111111111112", "0711111112");
        em.merge(pacientTest);

        //DELETE
        System.out.println("delete");
        Echipament echipamentToRemove = em.find(Echipament.class, 15);
        em.remove(echipamentToRemove);
        Medicament medicamentToRemove = em.find(Medicament.class, 9);
        em.remove(medicamentToRemove);
        Programare programareToRemove = em.find(Programare.class, 8);
        em.remove(programareToRemove);

//        //end
        System.out.println("END Tranzactie: CREATE-UPDATE-DELETE -------------------");
        em.getTransaction().commit();

        //READ
        System.out.println("read programari");
        em.clear();
        programari = queryProgramari.getResultList();
        for(Programare p: programari)
            System.out.println("programare entitate persistenta: " + p);
        em.close();
    }
}