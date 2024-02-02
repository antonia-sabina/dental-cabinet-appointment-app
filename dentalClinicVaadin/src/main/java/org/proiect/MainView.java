package org.proiect;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import org.proiect.programri.FormProgramareView;
import org.proiect.programri.NavigableGridProgramariView;
import org.proiect.views.pacienti.FormPacientView;
import org.proiect.views.pacienti.NavigableGridPacientiView;


@Route
public class MainView extends AppLayout implements RouterLayout {
    public MainView() {
        HorizontalLayout header = createHeader();
        addToNavbar(header);
        Tabs tabs = createTabs();
        addToDrawer(tabs);
    }

    private HorizontalLayout createHeader(){
        H1 title = new H1("Proiect Cabinet Dentar");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("90%");
        return header;
    }

    private Tabs createTabs(){
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.CALENDAR_CLOCK, "Lista Programari", NavigableGridProgramariView.class));
        tabs.add(createTab(VaadinIcon.EDIT, "Form Editare Programare", FormProgramareView.class));
        tabs.add(createTab(VaadinIcon.USERS, "Lista Pacienti", NavigableGridPacientiView.class));
        tabs.add(createTab(VaadinIcon.PENCIL, "Form Editare Pacient", FormPacientView.class));

        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass){
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        //
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}
