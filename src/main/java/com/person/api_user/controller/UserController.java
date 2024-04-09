package com.person.api_user.controller;

import com.person.api_user.domain.user.dto.UserResponseDto;
import com.person.api_user.domain.user.model.User;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.person.api_user.domain.user.repository.UserRepository;
import com.person.api_user.httpExceptions.EmailAlreadyExists;
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

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody User dataUser){
        
        Optional<User> existingUser = userRepository.findByEmail(dataUser.getEmail());

        if(existingUser.isPresent()){
            throw new EmailAlreadyExists();
        }

        User newUser = userRepository.save(dataUser);
        UserResponseDto userResponse = new UserResponseDto(newUser.getId(), newUser.getName(), newUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}



