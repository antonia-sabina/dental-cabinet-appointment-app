package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Tratament {
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idTratament;
    @NonNull private Double durataTratament; //in zile
    @NonNull @OneToMany
    private List<Medicament> medicamenteTratament = new ArrayList<Medicament>();

    @Setter(AccessLevel.NONE)
    private Double costTratament;
    private Double calculCostTratament(){
        Double cost = 0.0;
        for(Medicament medicamente: medicamenteTratament)
            cost += medicamente.getCostMedicament() * durataTratament;
        return cost;
    }

    private Double getCostTratament(){
        if(costTratament == null)
            costTratament = calculCostTratament();
        return costTratament;
    }

    private void adaugaMedicament(Medicament medicament){
        if (!this.medicamenteTratament.contains(medicament))
            this.medicamenteTratament.add(medicament);
    }

    private void stergeMedicament(Medicament medicament){
        if(this.medicamenteTratament.contains(medicament))
            this.medicamenteTratament.remove(medicament);
    }

    public Tratament(@NonNull Integer idTratament, @NonNull Double durataTratament, @NonNull List<Medicament> medicamenteTratament) {
        this.idTratament = idTratament;
        this.durataTratament = durataTratament;
        this.medicamenteTratament = medicamenteTratament;
    }
}

