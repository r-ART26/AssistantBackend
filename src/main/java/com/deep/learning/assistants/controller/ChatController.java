package com.deep.learning.assistants.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // Obtener los mensajes de un thread específico
    @GetMapping
    public ResponseEntity<List<Chat>> getChatsByThreadId(@RequestParam String threadId) {
        List<Chat> chats = this.cService.getByThreadId(threadId);
        if (chats.isEmpty()) {
            return ResponseEntity.noContent().build(); // o .ok(Collections.emptyList()) si prefieres
        }
        return ResponseEntity.ok(chats);
    }

    // Obtener mensaje por ID (opcional)
    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable Long id) {
        Optional<Chat> chat = this.cService.getById(id);
        return chat.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo mensaje en un thread
    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat created = this.cService.save(chat);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Editar un mensaje (opcional)
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

    // Eliminar mensaje por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        Optional<Chat> existing = this.cService.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        this.cService.delete(id);
        return ResponseEntity.noContent().build();
    }

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
        if (chatUpdates.getQuestion() != null) {
            existing.setQuestion(chatUpdates.getQuestion());
        }
        if (chatUpdates.getAnswer() != null) {
            existing.setAnswer(chatUpdates.getAnswer());
        }

        Chat updated = this.cService.save(existing);
        return ResponseEntity.ok(updated);
    }
}
