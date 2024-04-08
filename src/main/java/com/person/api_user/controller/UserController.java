package com.person.api_user.controller;

import com.person.api_user.domain.user.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.api_user.domain.user.repository.UserRepository;
import com.person.api_user.httpExceptions.UserNotFoudException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User detail(@PathVariable Integer id){
        return userRepository.findById(id)
        .orElseThrow(()-> new UserNotFoudException());
    }

    @GetMapping
    public List<User> listAll(){
        return userRepository.findAll();
    }
}
