package com.deep.learning.assistants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deep.learning.assistants.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
