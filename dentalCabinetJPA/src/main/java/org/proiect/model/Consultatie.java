package org.proiect.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Consultatie extends ServiciuOferit {
    private String descriereConsultatie;
    @Enumerated
    private SpecializareMedic domeniuConsultatie;

    public Consultatie(@NonNull Integer idServiciu, @NonNull String denumireServiciu, @NonNull Integer durataEstimata, @NonNull Double costServiciu, String descriereConsultatie, SpecializareMedic domeniuConsultatie) {
        super(idServiciu, denumireServiciu, durataEstimata, costServiciu);
        this.descriereConsultatie = descriereConsultatie;
        this.domeniuConsultatie = domeniuConsultatie;
    }
}