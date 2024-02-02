package org.proiect.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class ConsultatiePrestata extends ServiciuPrestat{
    private String diagnostic;
    @ManyToOne private Tratament tratamentRecomandat;

    public ConsultatiePrestata(@NonNull Integer idServiciuPrestat, @NonNull Programare programareServiciu, @NonNull String observatii, String diagnostic, Tratament tratamentRecomandat) {
        super(idServiciuPrestat, programareServiciu, observatii);
        this.diagnostic = diagnostic;
        this.tratamentRecomandat = tratamentRecomandat;
    }
}