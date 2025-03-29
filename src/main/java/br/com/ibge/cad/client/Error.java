package br.com.ibge.cad.client;

public record Error<S, E>(E value) implements ClientResult<S, E> {
    public Error {
        if (value == null) throw new IllegalArgumentException("Error value cannot be null");
    }

    @Override
    public boolean isSuccess() { return false; }

    @Override
    public boolean isError() { return true; }

    @Override
    public S success() { throw new IllegalStateException("No success present in Error"); }

    @Override
    public E error() { return value; }
}
