package com.maolabs.maobank.controller.dto.output;

import com.maolabs.maobank.model.Endereco;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoDtoOutput {
    private String cep;
    private String rua;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;

    public EnderecoDtoOutput(Endereco endereco) {
        this.cep = endereco.getCep();
        this.rua = endereco.getRua();
        this.bairro = endereco.getBairro();
        this.complemento = endereco.getComplemento();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
    }
}
