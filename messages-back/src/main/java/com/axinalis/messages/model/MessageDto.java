package com.axinalis.messages.model;

import java.util.Objects;

public class MessageDto {

    private String messageBody;

    public MessageDto() {
    }

    public MessageDto(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDto that = (MessageDto) o;
        return Objects.equals(messageBody, that.messageBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageBody);
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "messageBody='" + messageBody + '\'' +
                '}';
    }
}
