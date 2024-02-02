package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Medic extends Personal {
    @Enumerated
    private SpecializareMedic specializare;
    @OneToMany(mappedBy = "medicAsistat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistent> asistentiMedic = new ArrayList<Asistent>();
    @Enumerated
    private List<TipInterventie> interventiiPermise = new ArrayList<TipInterventie>();

    public void adaugaAsistent(Asistent asistent){
        if(!this.asistentiMedic.contains(asistent))
            this.asistentiMedic.add(asistent);
    }

    public void stergeAsistent(Asistent asistent){
        if(this.asistentiMedic.contains(asistent))
            this.asistentiMedic.remove(asistent);
    }

    public Medic(@NonNull Integer idPersonal, @NonNull String numePersonal, @NonNull String prenumePersonal, @NonNull String cnpPersonal, @NonNull String numarTelefonPersonal, SpecializareMedic specializare, List<Asistent> asistentiMedic, List<TipInterventie> interventiiPermise) {
        super(idPersonal, numePersonal, prenumePersonal, cnpPersonal, numarTelefonPersonal);
        this.specializare = specializare;
        this.asistentiMedic = asistentiMedic;
        this.interventiiPermise = interventiiPermise;
    }
}
