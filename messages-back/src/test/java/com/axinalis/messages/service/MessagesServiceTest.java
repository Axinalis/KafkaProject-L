package com.axinalis.messages.service;

import com.axinalis.messages.entity.MessageEntity;
import com.axinalis.messages.repository.MessagesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {MessagesService.class, ObjectMapper.class})
public class MessagesServiceTest {

    @Autowired
    private MessagesService messagesService;
    @MockBean
    private MessagesRepository messagesRepository;
    @MockBean
    private KafkaTemplate<String, String> template;

    @Test
    public void testSavingNewValidMessage(){
        // Given
        String messageText = "{\"messageBody\":\"Simple test message\"}";
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageBody("Simple test message");

        // When
        messagesService.saveNewMessage(messageText);

        // Then
        verify(messagesRepository, times(1)).save(messageEntity);
    }

    @Test
    public void testSavingNewInvalidMessage(){
        // Given
        String messageText = "{messageBody:Simple test message}";

        // When
        messagesService.saveNewMessage(messageText);

        // Then
        verify(messagesRepository, never()).save(any());
    }

    @Test
    public void testManageDeleteCommand(){
        // Given
        String messageText = "DELETE_MESSAGES";

        // When
        messagesService.manageCommand(messageText);

        // Then
        verify(messagesRepository, times(1)).deleteAll();
    }

    @Test
    public void testManageRequestCommand(){
        // Given
        String messageText = "REQUEST_MESSAGES";
        String messagesRespondTopic = "respond.message.test.topic";
        String messagesDtosListJson = "[{\"messageBody\":\"Message 1\"},{\"messageBody\":\"Message 2\"}]";
        MessageEntity entity1 = new MessageEntity(1L, "Message 1");
        MessageEntity entity2 = new MessageEntity(2L, "Message 2");
        List<MessageEntity> messagesList = List.of(entity1, entity2);
        doReturn(messagesList).when(messagesRepository).findAll();
        messagesService.setMessagesRespondTopic(messagesRespondTopic);

        // When
        messagesService.manageCommand(messageText);

        // Then
        verify(template, times(1)).send(messagesRespondTopic, messagesDtosListJson);
    }
}
