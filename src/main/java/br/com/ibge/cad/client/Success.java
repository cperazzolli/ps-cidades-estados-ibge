package br.com.ibge.cad.client;

public record Success<S, E>(S value) implements ClientResult<S, E> {
    public Success {
        if (value == null) throw new IllegalArgumentException("Success value cannot be null");
    }

    @Override
    public boolean isSuccess() { return true; }

    @Override
    public boolean isError() { return false; }

    @Override
    public S success() { return value; }

    @Override
    public E error() { throw new IllegalStateException("No error present in Success"); }
}
