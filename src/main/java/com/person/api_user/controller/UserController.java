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
import org.springframework.web.service.annotation.PutExchange;

import com.person.api_user.domain.user.repository.UserRepository;
import com.person.api_user.httpExceptions.EmailAlreadyExists;
import com.person.api_user.httpExceptions.UserNotFoudException;
import com.person.api_user.service.PasswordHashService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHashService passwordHashService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> detail(@PathVariable Integer id){
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isEmpty()){
            throw new UserNotFoudException();
        }

        User user = existingUser.get();

        UserResponseDto userResponse = new UserResponseDto(user.getId(), user.getName(),user.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
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

        String hashedPassword = passwordHashService.hashPassword(dataUser.getPassword());
        dataUser.setPassword(hashedPassword);

        User newUser = userRepository.save(dataUser);
        UserResponseDto userResponse = new UserResponseDto(newUser.getId(), newUser.getName(), newUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutExchange("/{id}")
    public ResponseEntity<UserResponseDto> update(@RequestBody User dataUser, @PathVariable Integer id){

        Optional<User> existngUser = userRepository.findById(id);

        if(existngUser.isEmpty()){
            throw new UserNotFoudException();
        }

        Optional<User> existingEmail = userRepository.findByEmail(dataUser.getEmail());

        if(existingEmail.isPresent()){
            throw new EmailAlreadyExists();
        }

        String hashedPassword = passwordHashService.hashPassword(dataUser.getPassword());

        User user = existngUser.get();

        user.setName(dataUser.getName());
        user.setEmail(dataUser.getEmail());
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}



