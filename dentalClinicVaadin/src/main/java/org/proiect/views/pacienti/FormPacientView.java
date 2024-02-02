package org.proiect.views.pacienti;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.proiect.MainView;
import org.proiect.model.Pacient;

@PageTitle("pacient")
@Route(value = "pacient", layout = MainView.class)
public class FormPacientView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Pacient pacient = null;
    private Binder<Pacient> binder = new BeanValidationBinder<>(Pacient.class);
    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Pagina Pacientului");
    private IntegerField idPacient = new IntegerField("ID: ");
    private TextField numePacient = new TextField("Nume: ");
    private TextField prenumePacient = new TextField("Prenume: ");
    private TextField cnpPacient = new TextField("CNP: ");
    private TextField numarTelefonPacient = new TextField("Numar de telefon: ");
    private Button cmdAdaugare = new Button("Adauga un nou pacient");

    private Button cmdSterge = new Button("Stergere");
    private Button cmdAbandon = new Button("Anulare");
    private Button cmdSalveaza = new Button("Salveaza");

    public FormPacientView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel(){
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        this.em = emf.createEntityManager();
        this.pacient = em.createQuery("SELECT p FROM Pacient p ORDER BY p.idPacient", Pacient.class).getResultStream().findFirst().get();
        binder.forField(idPacient).bind("idPacient");
        binder.forField(numePacient).bind("numePacient");
        binder.forField(prenumePacient).bind("prenumePacient");
        binder.forField(cnpPacient).bind("cnpPacient");
        binder.forField(numarTelefonPacient).bind("numarTelefonPacient");
        refreshForm();
    }

    private void initViewLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(idPacient, numePacient, prenumePacient, cnpPacient, numarTelefonPacient);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setMaxWidth("900px");
        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAbandon, cmdAdaugare, cmdSterge,cmdSalveaza);
        actionToolbar.setPadding(false);
        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);
        this.add(titluForm, formLayoutToolbar);
    }

    private void initControllerActions() {
        cmdAdaugare.addClickListener(e -> {
            adaugaPacient();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergePacient();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridPacientiView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridPacientiView.class, this.pacient.getIdPacient()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaPacient();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridPacientiView.class, this.pacient.getIdPacient()));
        });
    }

    private void refreshForm() {
        System.out.println("Pacient curent: " + this.pacient);
        if (this.pacient != null) {
            binder.setBean(this.pacient);
        }
    }

    private void salveazaPacient() {
        try {
            this.em.getTransaction().begin();
            this.pacient = this.em.merge(this.pacient);
            this.em.getTransaction().commit();
            System.out.println("Pacient Salvat");
        } catch (Exception ex) {
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            showNotificationError(ex);
        }
    }

    // CRUD actions

    private void adaugaPacient() {
        Integer idForm = (Integer) em.createQuery("SELECT MAX(p.idPacient) FROM Pacient p").getSingleResult() + 1;
        this.pacient = new Pacient();
        this.pacient.setIdPacient(idForm);
        this.pacient.setNumePacient("Pacient " + idForm);
        this.pacient.setPrenumePacient("Adaugat");
        this.pacient.setCnpPacient("2222222222222");
        this.pacient.setNumarTelefonPacient("0722222222");
        this.em.getTransaction().begin();
        this.pacient = this.em.merge(this.pacient);
        this.em.getTransaction().commit();
    }

    // CRUD actions
    private void stergePacient() {
        try {
            this.em.getTransaction().begin();
            if (this.em.contains(this.pacient)) {
                this.em.remove(this.pacient);
                this.em.getTransaction().commit();
                System.out.println("Pacient Sters");
            }
        }
        catch (Exception ex){
            if(this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            showNotificationError(ex);
        }
        System.out.println("To remove: " + this.pacient);
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
        System.out.println("Pacient ID: " + integer);
        if (integer != null) {
            this.pacient = em.find(Pacient.class, integer);
            System.out.println("Selectedd pacient to edit:: " + pacient);
            if (this.pacient == null) {
                System.out.println("ADD pacient:: " + pacient);
                this.adaugaPacient();
                this.pacient.setIdPacient(integer);
            }
        }
        this.refreshForm();
    }
}