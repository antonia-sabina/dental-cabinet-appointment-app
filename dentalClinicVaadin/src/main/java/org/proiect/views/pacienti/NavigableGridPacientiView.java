package org.proiect.views.pacienti;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.*;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.proiect.MainView;
import org.proiect.model.Pacient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("pacienti")
@Route(value = "pacienti", layout = MainView.class)
public class NavigableGridPacientiView extends VerticalLayout implements HasUrlParameter<Integer>{
    private EntityManager em;
    private List<Pacient> pacienti = new ArrayList<>();
    private Pacient pacient = null;
    private H1 titluForm = new H1("Lista Pacienti");
    private VerticalLayout gridLayoutToolbar;
    private TextField filterText = new TextField();
    private Button cmdAdaugaPacient = new Button("Adauga un nou pacient");
    private Grid<Pacient> grid = new Grid<>(Pacient.class);

    public NavigableGridPacientiView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private  void initDataModel(){
        System.out.println("DEBUG START FORM >>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProiectJPA");
        em = emf.createEntityManager();

        List<Pacient> lst = em.createQuery("SELECT p FROM Pacient p ORDER BY p.idPacient", Pacient.class).getResultList();
        pacienti.addAll(lst);

        if(lst != null && lst.isEmpty()){
            Collections.sort(this.pacienti, (p1, p2) -> p1.getIdPacient().compareTo(p2.getIdPacient()));
            this.pacient = pacienti.get(0);
            System.out.println("DEBUG: pacient init >>> " + pacient.getIdPacient() );
        }
        grid.setItems(this.pacienti);
        grid.asSingleSelect().setValue(this.pacient);
    }

    private void initViewLayout(){
        filterText.setPlaceholder("Filter by nume");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        HorizontalLayout gridToolbar = new HorizontalLayout(filterText, cmdAdaugaPacient);
        grid.setColumns("idPacient", "numePacient", "prenumePacient", "cnpPacient", "numarTelefonPacient");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni posibile");
        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
        this.add(titluForm, gridLayoutToolbar);
    }

    private Component createGridActionsButtons(Pacient item){
        Button cmdEditItem = new Button("Editeaza");
        cmdEditItem.addClickListener(e -> {grid.asSingleSelect().setValue(item); editPacient();});
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {System.out.println("sterge item: " + item); grid.asSingleSelect().setValue(item); stergePacient(); refreshForm();});
        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions(){
        filterText.addValueChangeListener(e -> updateList());
        cmdAdaugaPacient.addClickListener(e -> {adaugaPacient();});
    }
    private void adaugaPacient(){
        Integer idForm = (Integer) em.createQuery("SELECT MAX(p.idPacient) FROM Pacient p").getSingleResult() + 1;
        this.getUI().ifPresent(ui -> ui.navigate(FormPacientView.class, idForm));
    }

    private void editPacient(){
        this.pacient = this.grid.asSingleSelect().getValue();
        System.out.println("selected pacient:: " + pacient);
        if(this.pacient != null){
            this.getUI().ifPresent(ui -> ui.navigate(FormPacientView.class, this.pacient.getIdPacient()));
        }
    }

    private void stergePacient(){
        Pacient pacient = grid.asSingleSelect().getValue();
        System.out.println("to remove: " + pacient);
        em.getTransaction().begin();
        try{
            em.remove(pacient);
            em.getTransaction().commit();
            System.out.println("Pacient Sters");
        } catch (Exception ex){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            showNotificationError(ex);
        } finally {
            grid.asSingleSelect().clear();
            refreshData();
        }
    }

    private void refreshData(){
        pacienti = em.createQuery("SELECT p FROM Pacient p", Pacient.class).getResultList();
        grid.setItems(pacienti);
    }

    private void updateList(){
        try {
            List<Pacient> lstPacientiFiltered = this.pacienti;
            if(filterText.getValue() != null){
                lstPacientiFiltered = this.pacienti.stream().filter(p -> p.getNumePacient().contains(filterText.getValue())).toList();
                grid.setItems(lstPacientiFiltered);
            }
        }
        catch (Exception e){
            e.printStackTrace();
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

    private void refreshForm(){
        System.out.println("pacient curent: " + this.pacient);
        if(this.pacient != null){
            grid.setItems(this.pacienti);
            grid.select(this.pacient);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            this.pacient = em.find(Pacient.class, id);
            System.out.println("Back pacient: " + pacient);
            if (this.pacient == null) {
                // DELETED Item
                if (!this.pacienti.isEmpty())
                    this.pacient = this.pacienti.get(0);
            }
        }
        this.refreshForm();
    }
}