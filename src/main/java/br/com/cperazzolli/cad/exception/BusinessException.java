package br.com.cperazzolli.cad.exception;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 1149241039409861914L;

    public BusinessException(String msg){
        super(msg);
    }

    public BusinessException(String msg, Throwable cause){
        super(msg, cause);
    }
}
