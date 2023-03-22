package com.axinalis.messages.view;

import com.axinalis.messages.service.MessagesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/sender")
public class SenderView extends VerticalLayout {

    private MessagesService messagesService;

    @Autowired
    public SenderView(MessagesService messagesService) {
        this.messagesService = messagesService;
        Button senderButton = new Button("Sender");
        Button listButton = new Button("List");
        senderButton.addClickListener(event ->
                senderButton.getUI().ifPresent(ui ->
                        ui.navigate(SenderView.class)));
        listButton.addClickListener(event ->
                listButton.getUI().ifPresent(ui ->
                        ui.navigate(ListView.class)));
        setHeight("50%");
        senderButton.setSizeFull();
        senderButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        listButton.setSizeFull();

        MessageInput messageInput = new MessageInput();
        messageInput.setSizeFull();
        messageInput.addSubmitListener(submitEvent -> {
            messagesService.sendNewMessage(submitEvent.getValue());
            Notification.show("Message was sent");
        });

        HorizontalLayout buttons = new HorizontalLayout(senderButton, listButton);
        buttons.setWidth("100%");
        add(buttons, messageInput);
    }

}
