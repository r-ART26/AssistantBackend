package com.deep.learning.assistants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deep.learning.assistants.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
