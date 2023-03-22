package com.axinalis.messages.view;

import com.axinalis.messages.service.MessagesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/list")
public class ListView extends VerticalLayout {

    private Grid<Message> messages = new Grid<>(Message.class);
    private MessagesService messagesService;

    @Autowired
    public ListView(MessagesService messagesService) {
        this.messagesService = messagesService;
        Button senderButton = new Button("Sender");
        Button listButton = new Button("List");
        Button requestMessages = new Button("Update messages");
        Button clearMessages = new Button("Clear all");
        senderButton.addClickListener(event ->
                senderButton.getUI().ifPresent(ui ->
                        ui.navigate(SenderView.class)));
        listButton.addClickListener(event ->
                listButton.getUI().ifPresent(ui ->
                        ui.navigate(ListView.class)));
        requestMessages.addClickListener(event -> {
            messagesService.requestMessages();
            // I'm not proud of this part, but at least it works with fast enough Kafka
            try{
                Thread.sleep(350);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            getUI().ifPresent(ui -> ui.getPage().reload());
        });
        clearMessages.addClickListener(event -> {
            messagesService.deleteMessages();
            Notification.show("Messages are going to be deleted");
        });
        setHeight("100%");
        senderButton.setSizeFull();
        listButton.setSizeFull();
        listButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearMessages.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout listButtons = new HorizontalLayout(requestMessages, clearMessages);
        HorizontalLayout navigationButtons = new HorizontalLayout(senderButton, listButton);
        navigationButtons.setWidth("100%");

        messages.setItems(messagesService.getMostRecentMessages());

        add(navigationButtons, listButtons, messages);
    }
}
