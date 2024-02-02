package org.proiect.model;

import jakarta.validation.constraints.AssertFalse;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class InterventiePrestata extends ServiciuPrestat{
    @ManyToOne private Asistent asistentInterventie;
    private String complicatii;

    public void verificareAsistentInterventie(){
        Medic medicProgramare = this.getProgramareServiciu().getMedicProgramat();
        if(!asistentInterventie.getMedicAsistat().equals(medicProgramare))
            throw new IllegalStateException("asistentul nu poate participa la interventie");
    }

    public InterventiePrestata(@NonNull Integer idServiciuPrestat, @NonNull Programare programareServiciu, @NonNull String observatii, Asistent asistentInterventie, String complicatii) {
        super(idServiciuPrestat, programareServiciu, observatii);
        this.asistentInterventie = asistentInterventie;
        this.complicatii = complicatii;
    }
}
