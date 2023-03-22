package com.axinalis.messages.consumer;

import com.axinalis.messages.service.MessagesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {MessagesListListener.class, ObjectMapper.class})
class MessagesListListenerTest {

    @MockBean
    private MessagesService messagesService;
    @Autowired
    private MessagesListListener messagesListListener;

    @Test
    public void testListenMethod() {
        // Given
        String message = "Simple test message";

        // When
        messagesListListener.listen(message);

        // Then
        verify(messagesService, times(1)).updateRecentMessages(message);
    }

}