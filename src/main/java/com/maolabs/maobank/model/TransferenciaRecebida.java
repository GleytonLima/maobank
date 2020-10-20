package com.maolabs.maobank.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class TransferenciaRecebida extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private LocalDate dataRealizacao;

    @NotEmpty
    @NotNull
    private String documentoIdentificadorOrigem;

    @NotEmpty
    @NotNull
    private String bancoOrigem;

    @NotEmpty
    @NotNull
    private String contaOrigem;

    @NotEmpty
    @NotNull
    private String agenciaOrigem;

    @NotEmpty
    @NotNull
    private String codigoUnico;

    @NotEmpty
    @NotNull
    private String contaDestino;

    @NotEmpty
    @NotNull
    private String agenciaDestino;

    @OneToOne(fetch = FetchType.LAZY)
    private Movimentacao movimentacao;

}
