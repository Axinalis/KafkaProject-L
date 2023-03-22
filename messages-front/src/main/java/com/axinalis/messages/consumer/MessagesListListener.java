package com.axinalis.messages.consumer;

import com.axinalis.messages.service.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessagesListListener {

    private static Logger log = LoggerFactory.getLogger(MessagesListListener.class);
    private final MessagesService messagesService;

    @Autowired
    public MessagesListListener(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @KafkaListener(topics = "${topic.messages.respond}")
    public void listen(String message){
        log.info("Message was received");
        messagesService.updateRecentMessages(message);
    }

}