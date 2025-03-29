package br.com.ibge.cad.client;


public sealed interface ClientResult<S, E> permits Success, Error {

    boolean isSuccess();
    boolean isError();
    S success();
    E error();

    static <S, E> ClientResult<S, E> ok(S value) {
        return new Success<>(value);
    }

    static <S, E> ClientResult<S, E> fail(E value) {
        return new Error<>(value);
    }
}
