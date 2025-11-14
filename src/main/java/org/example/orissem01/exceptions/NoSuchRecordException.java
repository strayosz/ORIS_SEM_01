package org.example.orissem01.exceptions;

public class NoSuchRecordException extends Exception {
    public NoSuchRecordException() {
        super("Пользователя с таким логином не существует");
    }
}
