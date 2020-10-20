package com.maolabs.maobank.model;

import com.maolabs.maobank.util.customvalidators.PasswordGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Audited
@AuditTable(value="conta_AUDITORIA")
public class Conta extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String numeroAgencia;

    @NotNull
    private String numeroConta;

    @NotNull
    private String codigoBanco;

    @NotNull
    private BigDecimal saldo;

    @OneToOne
    private Proposta proposta;

    @OneToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @Setter(AccessLevel.NONE)
    private String senha;

    public void setSenha(String senha, PasswordEncoder passwordEncoder) {
        this.senha = passwordEncoder.encode(senha);
    }

    public static Conta gerarNovaConta(Proposta proposta, PasswordEncoder passwordEncoder) {
        Conta conta = new Conta();
        conta.setProposta(proposta);
        conta.setCliente(proposta.getCliente());
        //conta.setNumeroAgencia(String.format("%04d", new Random().nextInt(10000)));
        //conta.setNumeroConta(String.format("%08d", new Random().nextInt(100000000)));
        conta.setNumeroAgencia("0001");
        conta.setNumeroConta("11111111");
        conta.setCodigoBanco("341");
        conta.setSenha(PasswordGenerator.generatePasswordByLength(8), passwordEncoder);
        conta.setSaldo(BigDecimal.ZERO);
        return conta;
    }
}
