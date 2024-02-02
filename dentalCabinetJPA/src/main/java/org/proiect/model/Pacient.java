package org.proiect.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.TemporalType.DATE;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Pacient implements Comparable<Pacient> {
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idPacient;
    @NonNull private String numePacient;
    @NonNull private String prenumePacient;
    @NonNull private String cnpPacient;
    @NonNull private String numarTelefonPacient;

    public void setCnpPacient(String cnpPacient){
        if(cnpPacient != null && cnpPacient.matches("\\d{13}"))
            this.cnpPacient = cnpPacient;
        else throw new IllegalArgumentException("CNP-ul trebuie sa aiba 13 cifre");
    }

    public boolean verificaNumarTelefonPacient(String numarTelefonPacient){
        String format = "^07[0-9]{8}$";
        return numarTelefonPacient.matches(format);
    }

    public void setNumarTelefonPacient(String numarTelefonPacient){
        if(numarTelefonPacient != null && verificaNumarTelefonPacient(numarTelefonPacient))
            this.numarTelefonPacient = numarTelefonPacient;
        else throw new IllegalArgumentException("numar de telefon invalid");
    }

    @Override
    public int compareTo(Pacient o) {
        return this.cnpPacient.compareTo(o.getCnpPacient());
    }
}
