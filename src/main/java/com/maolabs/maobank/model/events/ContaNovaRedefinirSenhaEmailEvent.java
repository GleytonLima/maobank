package com.maolabs.maobank.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContaNovaRedefinirSenhaEmailEvent {

    private String para;
    private String assunto;
    private String mensagem;

}