package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.AUTO;

@Entity @Inheritance(strategy = InheritanceType.JOINED)
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor

public class ServiciuOferit implements Comparable<ServiciuOferit>{
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idServiciu;
    @NonNull private String denumireServiciu;
    @NonNull private Integer durataEstimata; //in minute
    @NonNull private Double costServiciu;

    public void setDurataEstimata(Integer durataEstimata){
        if(durataEstimata < 0)
            throw  new IllegalArgumentException("durata trebuie sa fie pozitiva");
        this.durataEstimata = durataEstimata;
    }

    @Override
    public int compareTo(ServiciuOferit o) {
        return this.denumireServiciu.compareTo(o.denumireServiciu);
    }
}