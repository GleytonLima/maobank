package com.maolabs.maobank.model;

import com.maolabs.maobank.util.customvalidators.MaiorDezoitoAnosContraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Audited
@AuditTable(value="cliente_AUDITORIA")
public class Cliente extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Não não pode estar em branco")
    private String nome;

    @NotNull(message = "Sobrenome é obrigatório")
    @NotBlank(message = "Sobrenome não pode estar em branco")
    private String sobrenome;

    @NotNull(message = "Email é obrigatório")
    @NotBlank(message = "Email não pode estar em branco")
    @Email(message = "Formato de email inválido")
    @Column(unique=true)
    private String email;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado.")
    @MaiorDezoitoAnosContraint
    private LocalDate dataNascimento;

    @NotNull(message = "CPF é obrigatório")
    @NotBlank(message = "CPF não pode estar em branco")
    @CPF(message = "CPF inválido")
    @Pattern(message = "Deve ter 11 digitos (sem pontos ou hifem)", regexp="[\\d]{11}")
    @Column(unique=true)
    private String cpf;
}
