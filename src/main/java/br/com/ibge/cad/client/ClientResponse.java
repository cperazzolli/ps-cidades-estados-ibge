package br.com.ibge.cad.client;

import br.com.ibge.cad.exception.BusinessException;

public class ClientResponse<S,E> {

    private final S success;
    private final E error;

    public ClientResponse(S success, E error) {
        this.success = success;
        this.error = error;
    }

    public static <S,E> ClientResponse<S,E> withSuccess(final S success) {
       if(success == null) {
           throw new BusinessException("the 'success' object cannot be constructed a null value");
       }

       return new ClientResponse<>(success,null);
    }

    public static <S,E> ClientResponse<S,E> withError(final E error) {
        if(error == null) {
            throw new BusinessException("the 'error' object cannot be constructed a null value");
        }

        return new ClientResponse<>(null,error);
    }

    public S success() {
        if(hasError()) {
            throw new BusinessException("the 'success' object cannot be constructed a null value");
        }

        return this.success;
    }

    public E error() {
        if(hasSuccess()) {
            throw new BusinessException("the 'error' object cannot be constructed a null value");
        }

        return this.error;
    }

    private boolean hasSuccess() {
        return this.success != null;
    }

    private boolean hasError() {
        return this.error != null;
    }

}
