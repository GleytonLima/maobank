package com.maolabs.maobank.controller.dto.input;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.util.customvalidators.MaiorDezoitoAnosContraint;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropostaInformacoesBasicasDtoInput {

    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Não não pode estar em branco")
    private String nome;

    @NotNull(message = "Sobrenome é obrigatório")
    @NotBlank(message = "Sobrenome não pode estar em branco")
    private String sobrenome;

    @NotNull(message = "Email é obrigatório")
    @NotBlank(message = "Email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado.")
    @MaiorDezoitoAnosContraint
    private LocalDate dataNascimento;

    @NotNull(message = "CPF é obrigatório")
    @NotBlank(message = "CPF não pode estar em branco")
    @CPF(message = "CPF inválido")
    @Pattern(message = "Deve ter 11 digitos (sem pontos ou hifem)", regexp="[\\d]{11}")
    private String cpf;

    public Cliente build() {
        var cliente  = new Cliente();
        cliente.setNome(this.nome);
        cliente.setSobrenome(this.sobrenome);
        cliente.setEmail(this.email);
        cliente.setDataNascimento(this.dataNascimento);
        cliente.setCpf(this.cpf);
        return cliente;
    }
}
