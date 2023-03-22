package com.axinalis.messages.consumer;

import com.axinalis.messages.service.MessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessagesListener {

    @Value("${topic.messages.new}")
    private String newMessagesTopic;
    @Value("${topic.messages.manage}")
    private String manageMessagesTopic;

    private Logger log = LoggerFactory.getLogger(MessagesListener.class);

    private MessagesService messagesService;

    @Autowired
    public MessagesListener(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    public void setNewMessagesTopic(String newMessagesTopic) {
        this.newMessagesTopic = newMessagesTopic;
    }

    public void setManageMessagesTopic(String manageMessagesTopic) {
        this.manageMessagesTopic = manageMessagesTopic;
    }

    @KafkaListener(topics = {"${topic.messages.new}", "${topic.messages.manage}"})
    public void listen(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Payload String message){
        if(topic.equals(newMessagesTopic)){
            log.info("New message was received");
            messagesService.saveNewMessage(message);
        } else if (topic.equals(manageMessagesTopic)) {
            log.info("A command was received: {}", message);
            messagesService.manageCommand(message);
        }
    }

}
