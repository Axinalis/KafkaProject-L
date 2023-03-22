package com.axinalis.messages.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class MainView extends VerticalLayout {

    @Autowired
    public MainView(){
        Button toSender = new Button("Sender");
        toSender.addClickListener(event ->
                toSender.getUI().ifPresent(ui ->
                        ui.navigate(SenderView.class)));
        Button toList = new Button("List");
        toList.addClickListener(event ->
                toList.getUI().ifPresent(ui ->
                        ui.navigate(ListView.class)));
        toSender.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        toSender.setWidth("20%");
        toList.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        toList.setWidth("20%");

        setHorizontalComponentAlignment(Alignment.CENTER, toSender, toList);

        add(toSender, toList);
    }

}
