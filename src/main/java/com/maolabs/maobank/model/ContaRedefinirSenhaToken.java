package com.maolabs.maobank.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Audited
@AuditTable(value="conta_redefinir_senha_AUDITORIA")
public class ContaRedefinirSenhaToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String token;

    @OneToOne(targetEntity = Conta.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "conta_id")
    private Conta conta;

    @NotNull
    private LocalDateTime expiryDate;

    public ContaRedefinirSenhaToken() {
    }

    public ContaRedefinirSenhaToken(Conta conta) {
        this.conta = conta;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusMinutes(10);
    }

    public boolean isTokenExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
