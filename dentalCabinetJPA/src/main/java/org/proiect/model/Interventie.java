package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Interventie extends ServiciuOferit {
    @Enumerated
    private TipInterventie tipInterventie;
    @OneToMany
    private List<Echipament> echipamenteInterventie = new ArrayList<Echipament>();

    public void adaugareEchipament(Echipament echipament) {
        if (!this.echipamenteInterventie.contains(echipament))
            this.echipamenteInterventie.add(echipament);
    }

    public void stergeEchipament(Echipament echipament) {
        if (this.echipamenteInterventie.contains(echipament))
            this.echipamenteInterventie.remove(echipament);
    }

    public Interventie(@NonNull Integer idServiciu, @NonNull String denumireServiciu, @NonNull Integer durataEstimata, @NonNull Double costServiciu, TipInterventie tipInterventie, List<Echipament> echipamenteInterventie) {
        super(idServiciu, denumireServiciu, durataEstimata, costServiciu);
        this.tipInterventie = tipInterventie;
        this.echipamenteInterventie = echipamenteInterventie;
    }
}