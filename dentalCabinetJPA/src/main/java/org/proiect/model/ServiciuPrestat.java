package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.AUTO;

@Entity @Inheritance(strategy = InheritanceType.JOINED)
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class ServiciuPrestat implements Comparable<ServiciuPrestat>{
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idServiciuPrestat;
    @NonNull
    @OneToOne @JoinColumn(name = "programareserviciu_idprogramare", referencedColumnName = "idProgramare")
    private Programare programareServiciu;
    @NonNull private String observatii;

    public void setProgramareServiciu(Programare programareServiciu){
        this.programareServiciu = programareServiciu;
        programareServiciu.setServiciuPrestat(this);
    }

    @Override
    public int compareTo(ServiciuPrestat o) {
        return this.programareServiciu.compareTo(o.programareServiciu);
    }
}
