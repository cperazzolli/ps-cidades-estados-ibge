package br.com.ibge.cad.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public record ClientError(
        String message,
        HttpStatusCode statusCode
) {
    public static ClientError with(final String message,final HttpStatusCode statusCode) {
        return new ClientError(message,statusCode);
    }

    public HttpStatus httpStatus(){
        return HttpStatus.valueOf(statusCode.value());
    }
}
