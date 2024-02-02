package org.proiect.model.repository;

import org.proiect.model.Medic;
import org.proiect.model.Pacient;
import org.proiect.model.Programare;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IRepositoryProgramari {
    Long getNumarProgramari();
    Programare getProgramareDupaData(Date data);
    List<Programare> getProgramariDupaData(Date date);
    Programare getProgramareDupaPacient(Pacient pacient);
    List<Programare> getProgramariDupaPacient(Pacient pacient);
    Programare getProgramareDupaMedic(Medic medic);
    List<Programare> getProgramariDupaMedic(Medic medic);
    Programare getProgramareDupaId(Integer id);
    void refresh(Programare programare);
    void remove(Programare programare);
    void add(Programare programare);
    Collection<Programare> getAll();
    void removeAll();
}
