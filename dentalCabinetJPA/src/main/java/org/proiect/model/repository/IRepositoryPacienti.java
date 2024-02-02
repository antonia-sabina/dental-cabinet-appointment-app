package org.proiect.model.repository;

import org.proiect.model.Pacient;

import java.util.Collection;
import java.util.List;

public interface IRepositoryPacienti {
    Long getNumarPacienti();
    Pacient getPacientDupaNume(String nume);
    Pacient getPacientDupaPrenume(String prenume);
    Pacient getPacientDupaNumeSiPrenume(String nume, String prenume);
    List<Pacient> getPacientiDupaNume(String nume);
    List<Pacient> getPacientiDupaPrenume(String prenume);
    List<Pacient> getPacientiDupaNumeSiPrenume(String nume, String prenume);
    Pacient getPacientDupaCnp(Long cnp);
    Pacient getPacientDupaNumarTelefon(String numarTelefon);
    Pacient getPacientDupaId(Integer id);
    void refresh(Pacient pacient);
    void remove(Pacient pacient);
    void add(Pacient pacient);
    Collection<Pacient> getAll();
    void removeAll();

}
