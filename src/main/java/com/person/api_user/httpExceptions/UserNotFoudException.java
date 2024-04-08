package com.person.api_user.httpExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoudException extends RuntimeException {
    public UserNotFoudException(){
        super("Usuário não encontrado");
    }
}
