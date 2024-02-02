package org.proiect.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Inheritance(strategy = InheritanceType.JOINED)
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Personal implements  Comparable<Personal>{
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idPersonal;
    @NonNull private String numePersonal;
    @NonNull private String prenumePersonal;
    @NonNull private String cnpPersonal;
    @NonNull private String numarTelefonPersonal;

    public void setCnpPersonal(String cnpPersonal){
        if(cnpPersonal != null && cnpPersonal.matches("\\d{13}"))
            this.cnpPersonal = cnpPersonal;
        else throw new IllegalArgumentException("CNP-ul trebuie sa aiba 13 cifre");
    }

    public boolean verificaNumarTelefonPersonal(String numarTelefonPersonal){
        String format = "^07[0-9]{8}$";
        return numarTelefonPersonal.matches(format);
    }

    public void setNumarTelefonPersonal(String numarTelefonPersonal){
        if(numarTelefonPersonal != null && verificaNumarTelefonPersonal(numarTelefonPersonal))
            this.numarTelefonPersonal = numarTelefonPersonal;
        else throw new IllegalArgumentException("numar de telefon invalid");
    }

    @Override
    public int compareTo(Personal o) {
        return this.cnpPersonal.compareTo(o.getCnpPersonal());
    }
}
