package com.axinalis.messages.service;

import com.axinalis.messages.entity.MessageEntity;
import com.axinalis.messages.model.MessageDto;
import com.axinalis.messages.repository.MessagesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {

    private static Logger log = LoggerFactory.getLogger(MessagesService.class);

    @Value("${topic.messages.respond}")
    private String messagesRespondTopic;

    private MessagesRepository messagesRepository;
    private KafkaTemplate<String, String> template;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MessagesService(MessagesRepository messagesRepository, KafkaTemplate<String, String> template) {
        this.messagesRepository = messagesRepository;
        this.template = template;
    }

    public void setMessagesRespondTopic(String messagesRespondTopic) {
        this.messagesRespondTopic = messagesRespondTopic;
    }

    public void saveNewMessage(String message){
        MessageDto messageDto = null;
        try {
            messageDto = objectMapper.readValue(message, new TypeReference<MessageDto>() {});
        } catch (JsonProcessingException e) {
            log.error("Parsing of new message failed: {}", e.getMessage());
            e.printStackTrace();
            return;
        }
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageBody(messageDto.getMessageBody());
        messagesRepository.save(messageEntity);
    }

    public void manageCommand(String command){
        if(command == null){
            log.warn("Empty command was passed");
            return;
        }
        if(command.equals("REQUEST_MESSAGES")){
            sendListOfMessages();
        } else if (command.equals("DELETE_MESSAGES")) {
            log.info("All messages are going to be deleted");
            messagesRepository.deleteAll();
        }
    }

    private void sendListOfMessages() {
        List<MessageEntity> messageEntityList = messagesRepository.findAll();
        List<MessageDto> messageDtos = messageEntityList.stream()
                .map(messageItem -> new MessageDto(messageItem.getMessageBody()))
                .toList();

        try {
            template.send(messagesRespondTopic,
                    objectMapper.writeValueAsString(messageDtos));
        } catch (JsonProcessingException e) {
            log.error("Writing the list of messages failed: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
