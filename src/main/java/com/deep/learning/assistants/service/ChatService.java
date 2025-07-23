package com.deep.learning.assistants.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deep.learning.assistants.model.Chat;
import com.deep.learning.assistants.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository cRepository;

    public List<Chat> getAll() {
        return this.cRepository.findAll();
    }

    public Optional<Chat> getById(Long id) {
        return this.cRepository.findById(id);
    }

    public List<Chat> getByThreadId(String threadId) {
        return this.cRepository.findByThreadId(threadId);
    }

    public Chat save(Chat chat) {
        return this.cRepository.save(chat);
    }

    public void delete(Long id) {
        this.cRepository.deleteById(id);
    }

}
