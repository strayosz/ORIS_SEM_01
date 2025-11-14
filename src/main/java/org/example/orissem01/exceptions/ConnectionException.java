package org.example.orissem01.exceptions;

public class ConnectionException extends Exception {
    public ConnectionException() {
        super("Ошибка при подключении, попробуйте еще раз...");
    }
}
