package org.example.orissem01.exceptions;

public class DublicateUserException extends Exception {
    public DublicateUserException() {
        super("Пользователь с таким логином уже существует");
    }
}
