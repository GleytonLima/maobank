package com.maolabs.maobank.controller.dto.output;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Endereco;
import com.maolabs.maobank.model.Proposta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PropostaDtoOutput {

    private Long id;
    private String propostaStatus;
    private ClienteDtoOutput cliente;
    private EnderecoDtoOutput endereco;

    public PropostaDtoOutput(Proposta proposta, Endereco endereco) {
        this.id = proposta.getId();
        this.propostaStatus = proposta.getPropostaStatus().name();
        this.cliente = new ClienteDtoOutput(proposta.getCliente());
        this.endereco = new EnderecoDtoOutput(endereco);
    }

}
