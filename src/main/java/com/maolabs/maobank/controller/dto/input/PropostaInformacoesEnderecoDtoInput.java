package com.maolabs.maobank.controller.dto.input;

import com.maolabs.maobank.model.Endereco;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropostaInformacoesEnderecoDtoInput {
    private String cep;
    private String rua;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;

    public Endereco build() {
        Endereco endereco = new Endereco();
        endereco.setCep(this.cep);
        endereco.setRua(this.rua);
        endereco.setBairro(this.bairro);
        endereco.setComplemento(this.complemento);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        return endereco;
    }
}
