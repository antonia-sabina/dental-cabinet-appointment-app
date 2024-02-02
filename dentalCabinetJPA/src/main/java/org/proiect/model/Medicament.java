package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Medicament implements Comparable<Medicament>{
    @EqualsAndHashCode.Include
    @Id @NonNull private Integer idMedicament;
    @NonNull private String denumireMedicament;
    @NonNull @Enumerated private FormaMedicament formaMedicament;
    @NonNull @Enumerated private ModAdministrare modDeAdministrare;
    @NonNull private Double costMedicament;

    @Override
    public int compareTo(Medicament o) {
        return this.idMedicament.compareTo(o.idMedicament);
    }
}
