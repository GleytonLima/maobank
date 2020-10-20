package com.maolabs.maobank.model.events;

import com.maolabs.maobank.model.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContaRedefinirSenhaEvent {

    private Conta conta;

}