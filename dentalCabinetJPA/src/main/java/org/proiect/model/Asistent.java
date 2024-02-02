package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Asistent extends Personal {
    @ManyToOne (cascade = CascadeType.REMOVE)
    private Medic medicAsistat; //poate asista un singur medic

    public Asistent(@NonNull Integer idPersonal, @NonNull String numePersonal, @NonNull String prenumePersonal, @NonNull String cnpPersonal, @NonNull String numarTelefonPersonal, Medic medicAsistat) {
        super(idPersonal, numePersonal, prenumePersonal, cnpPersonal, numarTelefonPersonal);
        this.medicAsistat = medicAsistat;
    }
}