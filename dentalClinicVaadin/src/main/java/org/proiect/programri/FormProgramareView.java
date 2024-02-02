package org.proiect.programri;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateTimeToDateConverter;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.proiect.MainView;
import org.proiect.model.*;
import org.proiect.views.pacienti.NavigableGridPacientiView;
import org.proiect.model.Programare;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@PageTitle("programare")
@Route(value = "programare", layout = MainView.class)
public class FormProgramareView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Programare programare = null;
    private Binder<Programare> binder = new BeanValidationBinder<>(Programare.class);
    private  VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Pagina Programarii");
    private IntegerField idProgramare = new IntegerField("ID: ");
    private DateTimePicker dataProgramare = new DateTimePicker("Data si ora programarii: ");
    private Select<StatusProgramare> statusProgramare = new Select<>();
    private IntegerField idPacient = new IntegerField("ID Pacient: ");
    private IntegerField idMedic = new IntegerField("ID Medic: ");
    private IntegerField serviciuProgramat = new IntegerField("ID serviciu: ");
    private Button cmdAdaugare = new Button("Adauga o noua programare");
    private Button cmdAbandon = new Button("Anulare");
    private Button cmdSterge = new Button("Stergere");
    private Button cmdSalveaza = new Button("Salveaza");

    public FormProgramareView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel(){
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        this.em = emf.createEntityManager();
        this.programare = em.createQuery("SELECT p FROM Programare p ORDER BY p.idProgramare", Programare.class).getResultStream().findFirst().orElse(new Programare());
        binder.forField(idProgramare).bind("idProgramare");
        binder.forField(dataProgramare).withConverter(new LocalDateTimeToDateConverter(ZoneId.systemDefault())).bind("dataProgramare");
        binder.forField(statusProgramare).asRequired("Selecteaza un status").bind("statusProgramare");
        statusProgramare.setItems(StatusProgramare.URMEAZA, StatusProgramare.ANULATA, StatusProgramare.FINALIZATA, StatusProgramare.IN_CURS);
        binder.forField(idPacient).bind(programare -> programare.getPacientProgramat().getIdPacient(), (programare, value) -> programare.setPacientProgramat(em.find(Pacient.class, value)));
        binder.forField(idMedic).bind(programare -> programare.getMedicProgramat().getIdPersonal(), (programare, value) -> programare.setMedicProgramat(em.find(Medic.class, value)));
        binder.forField(serviciuProgramat).bind(programare -> programare.getServiciuProgramat().getIdServiciu(), (programare, value) -> programare.setServiciuProgramat(em.find(ServiciuOferit.class, value)));
        refreshForm();
    }

    private void initViewLayout(){
        FormLayout formLayout = new FormLayout();
        formLayout.add(idProgramare, dataProgramare, new Text("Status programare: "),statusProgramare, idPacient, idMedic, serviciuProgramat);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setMaxWidth("700 px");
        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAbandon, cmdAdaugare, cmdSterge, cmdSalveaza);
        actionToolbar.setPadding(false);
        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);
        this.add(titluForm, formLayoutToolbar);
    }

    private void initControllerActions() {
        cmdAdaugare.addClickListener(e -> {
            adaugaProgramare();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeProgramare();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridPacientiView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridPacientiView.class, this.programare.getIdProgramare()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaProgramare();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridPacientiView.class, this.programare.getIdProgramare()));
        });
    }

    private void refreshForm() {
        System.out.println("Programare curenta; " + this.programare);
        if(this.programare != null){
            binder.setBean(this.programare);
        }
    }

    private void salveazaProgramare() {
        try {
            this.em.getTransaction().begin();
            this.programare = this.em.merge(this.programare);
            this.em.getTransaction().commit();
            System.out.println("Programare Salvata");
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void adaugaProgramare() {
        try{
            Integer idForm = (Integer) em.createQuery("SELECT MAX(p.idProgramare) FROM Programare p").getSingleResult() + 1;
            this.programare = new Programare();
            this.programare.setIdProgramare(idForm);
            this.programare.setDataProgramare(new Date());
            this.programare.setStatusProgramare(StatusProgramare.IN_CURS);
            this.programare.setPacientProgramat(this.em.find(Pacient.class, idPacient.getValue()));
            this.programare.setMedicProgramat(this.em.find(Medic.class, idMedic.getValue()));
            this.programare.setServiciuProgramat(this.em.find(ServiciuOferit.class, serviciuProgramat.getValue()));
            this.em.getTransaction().begin();
            this.programare = this.em.merge(this.programare);
            this.em.getTransaction().commit();
            System.out.println("Programare Adaugata");
        }
        catch (Exception ex){
            if(this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            showNotificationError(ex);
        }
    }

    private void stergeProgramare() {
        System.out.println("To remove: " + this.programare);
        try {
            this.em.getTransaction().begin();
            if (this.em.contains(this.programare)) {
                this.em.remove(this.programare);
                this.em.getTransaction().commit();
                System.out.println("Programare Stearsa");
            }
        } catch (IllegalArgumentException ex){
            if(this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            showNotificationError(ex);
        }
    }

    private void showNotificationError(Exception ex){
        String error = "Error: ";
        if(ex.getCause() instanceof ConstraintViolationException)
            error += getValidationMessages((ConstraintViolationException) ex.getCause());
        else
            error += ex.getMessage();
        //
        Notification notification = new Notification(error, 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    private String getValidationMessages(ConstraintViolationException ex){
        System.out.println("handleValidation: ");
        String validationMessages = "";
        for (ConstraintViolation<?> cv : ex.getConstraintViolations()){
            System.out.println("!!! ConstraintViolation: " + cv.getMessage());
            System.out.println("		Property validated: " + cv.getPropertyPath().toString());
            //
            validationMessages += cv.getMessage() + "\n";
        }
        return validationMessages;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Integer integer) {
        System.out.println("Programare ID: " + integer);
        if (integer != null) {
            this.programare = em.find(Programare.class, integer);
            System.out.println("Selected programare to edit:: " + programare);
            if (this.programare == null) {
                System.out.println("ADD programare:: " + programare);
                this.adaugaProgramare();
                this.programare.setIdProgramare(integer);
            }
        }
        this.refreshForm();
    }
}
