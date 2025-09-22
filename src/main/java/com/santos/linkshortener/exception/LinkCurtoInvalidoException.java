package com.santos.linkshortener.exception;

public class LinkCurtoInvalidoException extends RuntimeException{
    public LinkCurtoInvalidoException(String message) {
        super(message);
    }
}
