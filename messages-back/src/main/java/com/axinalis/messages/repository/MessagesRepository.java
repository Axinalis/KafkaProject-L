package com.axinalis.messages.repository;

import com.axinalis.messages.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<MessageEntity, Long> {
}
