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

@Entity
@Getter
@Setter
public class Movimentacao extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private BigDecimal valor;

    @NotNull
    @NotEmpty
    private String codigoUnico;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private MovimentacaoValorTipoEnum tipoValorEnum;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private MovimentacaoTipoEnum tipo;

    @OneToOne(targetEntity = Conta.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "conta_destino_id")
    private Conta contaDestino;
}
