package org.proiect.programri;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.proiect.MainView;
import org.proiect.model.Programare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("programari")
@Route(value = "programari", layout = MainView.class)
public class NavigableGridProgramariView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private List<Programare> programari = new ArrayList<>();
    private Programare programare = null;
    private H1 titluForm = new H1("Lista Programari");
    private VerticalLayout gridLayoutToolbar;
    private Button cmdAdaugaProgramare = new Button("Adauga o noua programare");
    private Grid<Programare> grid = new Grid<>(Programare.class);

    public NavigableGridProgramariView() {
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel() {
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        em = emf.createEntityManager();

        List<Programare> lst = em.createQuery("SELECT p FROM Programare p ORDER BY p.idProgramare", Programare.class).getResultList();
        programari.addAll(lst);

        if (lst != null && lst.isEmpty()) {
            Collections.sort(this.programari, (p1, p2) -> p1.getIdProgramare().compareTo(p2.getIdProgramare()));
            this.programare = programari.get(0);
            System.out.println("DEBUG: programare init >>> " + programare.getIdProgramare());
        }
        grid.setItems(this.programari);
        grid.asSingleSelect().setValue(this.programare);
    }

    private void initViewLayout() {
        HorizontalLayout gridToolbar = new HorizontalLayout(cmdAdaugaProgramare);
        grid.setColumns("idProgramare", "dataProgramare");
        grid.addColumn(programare -> programare.getPacientProgramat().getIdPacient().toString()).setHeader("ID Pacient");
        grid.addColumn(programare -> programare.getMedicProgramat().getIdPersonal().toString()).setHeader("ID Medic");
        grid.addColumn(programare -> programare.getServiciuProgramat().getIdServiciu().toString()).setHeader("Serviciu Programat");
        grid.addColumn(Programare::getStatusProgramare).setHeader("Status Programare");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni posibile");
        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
        this.add(titluForm, gridLayoutToolbar);
    }

    private Component createGridActionsButtons(Programare item) {
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editProgramare();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeProgramare();
            refreshForm();
        });
        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions() {
        cmdAdaugaProgramare.addClickListener(e -> {
            adaugaProgramare();
        });
    }

    private void adaugaProgramare() {
        Integer idForm = (Integer) em.createQuery("SELECT MAX(p.idProgramare) FROM Programare p").getSingleResult() + 1;
        this.getUI().ifPresent(ui -> ui.navigate(FormProgramareView.class, idForm));
    }

    private void editProgramare() {
        Programare programare = grid.asSingleSelect().getValue();
        em.detach(programare);
        em.getEntityManagerFactory().getCache().evictAll();
        this.getUI().ifPresent(ui -> {
            ui.navigate(FormProgramareView.class, programare.getIdProgramare());
        });
    }

    private void stergeProgramare() {
        Programare programare = grid.asSingleSelect().getValue();
        System.out.println("To remove: " + programare);
        em.getTransaction().begin();
        try {
            em.remove(programare);
            em.getTransaction().commit();
            System.out.println("Programare Stearsa");
        } catch (Exception ex) {
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            showNotificationError(ex);
        } finally {
            grid.asSingleSelect().clear();
            refreshData();
        }
    }

    private void refreshData() {
        programari = em.createQuery("SELECT p FROM Programare p", Programare.class).getResultList();
        grid.setItems(programari);
    }

    private void refreshForm(){
        System.out.println("programare curenta: " + this.programare);
        if(this.programare != null){
            grid.setItems(this.programare);
            grid.select(this.programare);
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
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            this.programare = em.find(Programare.class, id);
            System.out.println("Back programare: " + programare);
            if (this.programare == null) {
                // DELETED Item
                if (!this.programari.isEmpty())
                    this.programare = this.programari.get(0);
            }
        }
        this.refreshForm();
    }
}
