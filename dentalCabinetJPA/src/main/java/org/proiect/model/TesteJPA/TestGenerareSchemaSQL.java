package org.proiect.model.TesteJPA;

import jakarta.persistence.*;

public class TestGenerareSchemaSQL {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        EntityManager em = emf.createEntityManager();
        System.out.println();
    }
}