package com.person.api_user.httpExceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(){
        super("Email já cadastrado");
    }
}
