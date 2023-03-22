package com.axinalis.messages.service;

import com.axinalis.messages.view.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MessagesService.class, ObjectMapper.class})
public class MessageServiceTest {

    @MockBean
    private KafkaTemplate<String, String> template;
    @Autowired
    private MessagesService messagesService;

    @Test
    public void testSendNewMessage(){
        // Given
        String messageText = "Simple test message";
        String messageJson = "{\"messageBody\":\"Simple test message\"}";
        String newMessageTopic = "new.message.test.topic";
        messagesService.setNewMessageTopic(newMessageTopic);

        // When
        messagesService.sendNewMessage(messageText);

        // Then
        verify(template, times(1)).send(newMessageTopic, messageJson);
    }

    @Test
    public void testRequestMessages(){
        // Given
        String requestMessagesMessage = "REQUEST_MESSAGES";
        String manageMessagesTopic = "manage.message.test.topic";
        messagesService.setManageMessagesTopic(manageMessagesTopic);

        // When
        messagesService.requestMessages();

        //Then
        verify(template, times(1)).send(manageMessagesTopic, requestMessagesMessage);
    }

    @Test
    public void testDeletingMessages(){
        // Given
        String deleteMessagesMessage = "DELETE_MESSAGES";
        String manageMessagesTopic = "manage.message.test.topic";
        messagesService.setManageMessagesTopic(manageMessagesTopic);

        // When
        messagesService.deleteMessages();

        //Then
        verify(template, times(1)).send(manageMessagesTopic, deleteMessagesMessage);
    }

    @Test
    public void testUpdateRecentMessagesWithValidString(){
        // Given
        Message message1 = new Message();
        Message message2 = new Message();
        List<Message> messages = new ArrayList<>();
        message1.setMessageBody("Message 1");
        message2.setMessageBody("Message 2");
        messages.add(message1);
        messages.add(message2);
        String validMessages = "[{\"messageBody\":\"Message 1\"},{\"messageBody\":\"Message 2\"}]";

        // When
        messagesService.updateRecentMessages(validMessages);

        // Then
        assertEquals(messages, messagesService.getMostRecentMessages());
    }

    @Test
    public void testUpdateRecentMessagesWithInvalidString(){
        // Given
        String invalidMessages = "[{\"messageBody\"\"Message 1\",{\"messageBody\":\"Message 2\"}]";
        messagesService.getMostRecentMessages().clear();

        // When
        messagesService.updateRecentMessages(invalidMessages);

        // Then
        assertThat(messagesService.getMostRecentMessages()).isEmpty();
    }
}
