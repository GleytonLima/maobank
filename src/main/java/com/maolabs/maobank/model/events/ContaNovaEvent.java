package com.maolabs.maobank.model.events;

import com.maolabs.maobank.model.Proposta;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContaNovaEvent {

    private Proposta proposta;

}