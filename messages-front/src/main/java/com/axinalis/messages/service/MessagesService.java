package com.axinalis.messages.service;

import com.axinalis.messages.view.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MessagesService {

    private static Logger log = LoggerFactory.getLogger(MessagesService.class);

    @Value("${topic.messages.new}")
    private String newMessageTopic;
    @Value("${topic.messages.manage}")
    private String manageMessagesTopic;
    private KafkaTemplate<String, String> template;
    private ObjectMapper objectMapper;
    private List<Message> mostRecentMessages;

    @Autowired
    public MessagesService(KafkaTemplate<String, String> template, ObjectMapper objectMapper) {
        this.template = template;
        this.objectMapper = objectMapper;
        mostRecentMessages = new LinkedList<>();
    }

    public void setNewMessageTopic(String newMessageTopic) {
        this.newMessageTopic = newMessageTopic;
    }

    public void setManageMessagesTopic(String manageMessagesTopic) {
        this.manageMessagesTopic = manageMessagesTopic;
    }

    public void sendNewMessage(String newMessage){
        Message message = new Message();
        message.setMessageBody(newMessage);
        try{
            String jsonMessage = objectMapper.writeValueAsString(message);
            template.send(newMessageTopic, jsonMessage);
        } catch (JsonProcessingException e){
            log.error("Writing message as a string failed: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void requestMessages(){
        log.info("Requesting messages from back-end");
        template.send(manageMessagesTopic, "REQUEST_MESSAGES");
    }

    public void deleteMessages(){
        log.info("Requesting the deletion of all messages from database");
        template.send(manageMessagesTopic, "DELETE_MESSAGES");
    }

    public void updateRecentMessages(String messages){
        try{
            mostRecentMessages = objectMapper.readValue(messages, new TypeReference<List<Message>>() {});
        } catch (JsonProcessingException e) {
            log.error("Parsing list of messages from string failed: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Message> getMostRecentMessages() {
        return mostRecentMessages;
    }
}
