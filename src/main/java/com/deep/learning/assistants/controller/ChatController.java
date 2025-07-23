package com.deep.learning.assistants.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.deep.learning.assistants.model.Chat;
import com.deep.learning.assistants.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService cService;

    @GetMapping("/all")
    public ResponseEntity<List<Chat>> getAllChats() {
        List<Chat> chats = this.cService.getAll();
        return ResponseEntity.ok(chats);
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getChatsByThreadId(@RequestParam String threadId) {
        List<Chat> chats = this.cService.getByThreadId(threadId);
        if (chats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Optional<Chat> chat = this.cService.getById(id);
        return chat.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat created = this.cService.save(chat);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chat> updateChat(@PathVariable Long id, @RequestBody Chat chat) {
        Optional<Chat> existing = this.cService.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        chat.setId(id);
        Chat updated = this.cService.save(chat);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        Optional<Chat> existing = this.cService.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        this.cService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH por ID (agrega a las listas)
    @PatchMapping("/{id}")
    public ResponseEntity<Chat> patchChat(@PathVariable Long id, @RequestBody Chat chatUpdates) {
        Optional<Chat> existingOpt = this.cService.getById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Chat existing = existingOpt.get();

        if (chatUpdates.getThreadId() != null) {
            existing.setThreadId(chatUpdates.getThreadId());
        }

        if (chatUpdates.getQuestions() != null && !chatUpdates.getQuestions().isEmpty()) {
            existing.getQuestions().addAll(chatUpdates.getQuestions());
        }

        if (chatUpdates.getAnswers() != null && !chatUpdates.getAnswers().isEmpty()) {
            existing.getAnswers().addAll(chatUpdates.getAnswers());
        }

        Chat updated = this.cService.save(existing);
        return ResponseEntity.ok(updated);
    }

    // PATCH por threadId (agrega a las listas)
    @PatchMapping
    public ResponseEntity<List<Chat>> patchChatByThreadId(
            @RequestParam String threadId,
            @RequestBody Chat chatUpdates) {

        List<Chat> existingChats = this.cService.getByThreadId(threadId);

        if (existingChats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Chat chat : existingChats) {
            if (chatUpdates.getQuestions() != null && !chatUpdates.getQuestions().isEmpty()) {
                chat.getQuestions().addAll(chatUpdates.getQuestions());
            }

            if (chatUpdates.getAnswers() != null && !chatUpdates.getAnswers().isEmpty()) {
                chat.getAnswers().addAll(chatUpdates.getAnswers());
            }

            this.cService.save(chat);
        }

        return ResponseEntity.ok(existingChats);
    }
}
