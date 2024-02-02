package org.proiect.model.repository;

import org.proiect.model.ServiciuOferit;

import java.util.List;

public interface IRepositoryServiciiOferite {
    Long getNumarServiciiOferite();
    ServiciuOferit getServiciuDupaDenumire(String denumireServiciu);
    ServiciuOferit getServiciuDupaDurata(Integer durataEstimata);
    List<ServiciuOferit> getServiciiDupaDurataEstimata(Integer durataEstimata);
    ServiciuOferit getServiciuDupaId(Integer id);
    void refresh(ServiciuOferit serviciuOferit);
    void remove(ServiciuOferit serviciuOferit);
    void add(ServiciuOferit serviciuOferit);
    List<ServiciuOferit> getAll();
    void removeAll();

}
