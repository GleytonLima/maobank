package com.maolabs.maobank.controller.dto.output;

import com.maolabs.maobank.model.Cliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDtoOutput {
    private String nome;
    private String sobrenome;
    private String email;
    private LocalDate dataNascimento;
    private String cpf;

    public ClienteDtoOutput(Cliente cliente) {
        this.nome = cliente.getNome();
        this.sobrenome = cliente.getSobrenome();
        this.email = cliente.getEmail();
        this.dataNascimento = cliente.getDataNascimento();
        this.cpf = cliente.getCpf().substring(0, 3).concat("********");
    }
}
