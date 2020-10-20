package com.maolabs.maobank.model.exception;

public class TransicaoDeStatusNaoSuportadaException extends Throwable {
    public TransicaoDeStatusNaoSuportadaException(String mensagem) {
        super(mensagem);
    }
}
