package org.example.orissem01.exceptions;

public class MySQLException extends Exception {
    public MySQLException() {
        super("Что-то пошло не так, попробуйте еще раз...");
    }
}
