package com.deep.learning.assistants.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deep.learning.assistants.model.User;
import com.deep.learning.assistants.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository uRepository;

    public List<User> getAll() {
        return this.uRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return this.uRepository.findById(id);
    }

    public User save(User user) {
        return this.uRepository.save(user);
    }

    public void delete(Long id) {
        this.uRepository.deleteById(id);
    }

    public User addThread(Long userId, String threadId) {
        User user = this.uRepository.findById(userId).orElseThrow();
        user.getThreads().add(threadId);
        return this.uRepository.save(user);
    }
}
