package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Echipament implements Comparable<Echipament>{
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idEchipament;
    @NonNull private TipEchipament denumireEchipament;
    @NonNull private String descriereEchipament;


    @Override
    public int compareTo(Echipament o) {
        return this.denumireEchipament.compareTo(o.denumireEchipament);
    }
}