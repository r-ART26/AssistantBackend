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

import com.deep.learning.assistants.model.User;
import com.deep.learning.assistants.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService uService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = uService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = this.uService.getById(id);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/firebase/{idFirebase}")
    public ResponseEntity<User> getUserByFirebaseId(@PathVariable String idFirebase) {
        Optional<User> user = this.uService.getByIdFirebase(idFirebase);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = this.uService.save(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/threads")
    public ResponseEntity<User> addThreadToUser(@PathVariable Long id, @RequestParam String threadId) {
        Optional<User> userOpt = this.uService.getById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User updated = this.uService.addThread(id, threadId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = this.uService.getById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        this.uService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody User userUpdates) {
        Optional<User> existingOpt = this.uService.getById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existing = existingOpt.get();

        if (userUpdates.getUsername() != null) {
            existing.setUsername(userUpdates.getUsername());
        }

        if (userUpdates.getIdFirebase() != null) {
            existing.setIdFirebase(userUpdates.getIdFirebase());
        }

        if (userUpdates.getThreads() != null && !userUpdates.getThreads().isEmpty()) {
            // Evita duplicados
            for (String threadId : userUpdates.getThreads()) {
                if (!existing.getThreads().contains(threadId)) {
                    existing.getThreads().add(threadId);
                }
            }
        }

        User updated = this.uService.save(existing);
        return ResponseEntity.ok(updated);
    }
}
