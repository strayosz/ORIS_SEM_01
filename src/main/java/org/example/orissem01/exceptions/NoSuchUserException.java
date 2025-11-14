package org.example.orissem01.exceptions;

public class NoSuchUserException extends Exception {
    public NoSuchUserException() {
        super("Пользователя с таким логином не существует");
    }
}
