package br.com.ibge.cad.exception;

public class RetryExhaustedException extends RuntimeException {

    public RetryExhaustedException(String message) {
        super(message);
    }

    public RetryExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}