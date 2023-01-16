package kalo.main.controller;

import lombok.Getter;

@Getter
public class BasicException extends RuntimeException {

    public BasicException(String message) {
        super(message);
    }

}
