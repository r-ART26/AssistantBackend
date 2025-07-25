package com.deep.learning.assistants.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deep.learning.assistants.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdFirebase(String idFirebase);
}
