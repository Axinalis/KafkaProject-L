package com.axinalis.messages.consumer;

import com.axinalis.messages.service.MessagesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MessagesListener.class})
public class MessagesListenerTest {

    @Autowired
    private MessagesListener messagesListener;
    @MockBean
    private MessagesService messagesService;

    @Test
    public void testListenNewMessage(){
        // Given
        String newMessageTopic = "new.message.test.topic";
        String messageText = "Simple test message";
        messagesListener.setNewMessagesTopic(newMessageTopic);

        // When
        messagesListener.listen(newMessageTopic, messageText);

        // Then
        verify(messagesService, times(1)).saveNewMessage(messageText);
    }

    @Test
    public void testListenManagingMessage(){
        // Given
        String managingTopic = "manage.test.topic";
        String managingMessageText = "do-something message";
        messagesListener.setManageMessagesTopic(managingTopic);

        // When
        messagesListener.listen(managingTopic, managingMessageText);

        // Then
        verify(messagesService, times(1)).manageCommand(managingMessageText);
    }

}
