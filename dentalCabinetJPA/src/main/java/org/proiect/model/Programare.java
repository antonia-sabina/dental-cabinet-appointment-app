package org.proiect.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Programare implements Comparable<Programare>{
    @EqualsAndHashCode.Include
    @NonNull @Id @GeneratedValue(strategy = AUTO) private Integer idProgramare;
    @NonNull @Temporal(TemporalType.TIMESTAMP) private Date dataProgramare;
    @NonNull @ManyToOne private Pacient pacientProgramat;
    @NonNull @ManyToOne private Medic medicProgramat;
    @NonNull @ManyToOne private ServiciuOferit serviciuProgramat;
    @Enumerated
    @NonNull private StatusProgramare statusProgramare;
    @OneToOne (mappedBy = "programareServiciu")
    private ServiciuPrestat serviciuPrestat;
    private Date dataFinalizareProgramare;

    public Date getDataFinalizareProgramare(){
        Long d1 = this.dataProgramare.getTime() + (this.serviciuProgramat.getDurataEstimata() * 60000L); // inmultit cu 60000 pentru convertirea minutelor in milisecunde
        Date dataFinalizareProgramare = new Date(d1);
        return dataFinalizareProgramare;
    }


    public void setStatusProgramare(){
        Date dataCurenta = new Date();
        if(this.statusProgramare == StatusProgramare.ANULATA)
            return;
        if(dataCurenta.before(this.dataProgramare))
            this.statusProgramare = StatusProgramare.URMEAZA;
        Date dataFinalizareProgramare = getDataFinalizareProgramare();
        if(dataCurenta.after(this.dataProgramare) && dataCurenta.before(dataFinalizareProgramare)){
            this.statusProgramare = StatusProgramare.IN_CURS;
            return;
        }
        if (dataCurenta.after(dataFinalizareProgramare))
            this.statusProgramare = StatusProgramare.FINALIZATA;
    }

    public void anuleazaProgramarea(){
        if(this.statusProgramare == StatusProgramare.FINALIZATA)
            throw new IllegalStateException("o programare finalizata nu poate fi anulata");
        this.statusProgramare = StatusProgramare.ANULATA;
    }

    public void reprogrameazaProgramarea(Date dataNoua){
        if(this.statusProgramare != StatusProgramare.ANULATA)
            throw new IllegalStateException("programarea trebuie anulata inainte de a fi reprogramata");
        if(dataNoua == null || dataNoua.before(new Date(System.currentTimeMillis())))
            throw new IllegalStateException("data noua a programarii este invalida");
        this.dataProgramare = dataNoua;
        this.statusProgramare = StatusProgramare.URMEAZA;
    }

    public void verificaDisponibilitate(List<Programare> programariMedic, List<Programare> programariPacient, Date dataProgramare, Date dataFinalizareProgramare){
        if(!verificaDisponibilitateIndividuala(programariMedic, dataProgramare, dataFinalizareProgramare))
            throw new IllegalStateException("medicul nu este dispoibil pentru aceasta programare");
        if(!verificaDisponibilitateIndividuala(programariPacient, dataProgramare, dataFinalizareProgramare))
            throw new IllegalStateException("pacientul nu este disponibil pentru aceasta programare");
    }


    public static boolean verificaDisponibilitateIndividuala(List<Programare> programari, Date dataProgramarePropusa, Date dataFinalizareProgramarePropusa){
        for (Programare programare : programari) {
            if ((dataProgramarePropusa.after(programare.getDataProgramare()) && dataProgramarePropusa.before(programare.getDataFinalizareProgramare())) ||
                    (dataFinalizareProgramarePropusa.after(programare.getDataProgramare()) && dataFinalizareProgramarePropusa.before(programare.getDataFinalizareProgramare())) ||
                    (dataProgramarePropusa.before(programare.getDataProgramare()) && dataFinalizareProgramarePropusa.after(programare.getDataFinalizareProgramare())) ||
                    (dataProgramarePropusa.equals(programare.getDataProgramare()) ||
                            dataFinalizareProgramarePropusa.equals(programare.getDataProgramare()) ||
                            dataProgramarePropusa.equals(programare.getDataFinalizareProgramare())
                            || dataFinalizareProgramarePropusa.equals(programare.getDataFinalizareProgramare())))
                return false;
        }
        return true;
    }

    public void verificareMedicServiciu() {
        if (serviciuProgramat instanceof Consultatie) {
            Consultatie consultatie = (Consultatie) serviciuProgramat;
            if (!medicProgramat.getSpecializare().equals(consultatie.getDomeniuConsultatie()))
                throw new IllegalStateException("un medic trebuie sa aiba aceeasi specializare ca si domeniul consultatiei");
        }
        else if (serviciuProgramat instanceof Interventie) {
            Interventie interventie = (Interventie) serviciuProgramat;
            int k = 0;
            for (TipInterventie tipInterventie : getMedicProgramat().getInterventiiPermise())
                if (tipInterventie.equals(interventie.getTipInterventie()))
                {
                    k = 1;
                    break;
                }
            if (k == 0) throw new IllegalStateException("un medic poate realiza numai anumite tipuri de interventii");
        }
    }

    public void verificareOraProgramare(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataProgramare);
        int oraProgramare = calendar.get(Calendar.HOUR_OF_DAY);
        if(!(oraProgramare > 8 && oraProgramare < 18))
            throw new IllegalStateException("ora programarii trebuie sa fie intre 9:00 si 18:00");
    }

    @Override
    public String toString() {
        return "Programare: " +
                "idProgramare: " + idProgramare +
                ", dataProgramare: " + dataProgramare +
                ", pacientProgramat: " + pacientProgramat +
                ", serviciuProgramat: " + serviciuProgramat +
                ", statusProgramare: " + statusProgramare +
                '}';
    }

    @Override
    public int compareTo(Programare o) {
        return this.idProgramare.compareTo(o.idProgramare);
    }
}