package com.person.api_user.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.person.api_user.domain.user.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {

}
