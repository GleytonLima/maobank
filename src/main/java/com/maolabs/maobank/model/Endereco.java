package com.maolabs.maobank.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Audited
@AuditTable(value="endereco_AUDITORIA")
public class Endereco extends AbstractAuditingEntity {
    @Id
    private Long id;

    @NotNull
    @NotEmpty
    private String cep;

    @NotNull
    @NotEmpty
    private String rua;

    @NotNull
    @NotEmpty
    private String bairro;

    @NotNull
    @NotEmpty
    private String complemento;

    @NotNull
    @NotEmpty
    private String cidade;

    @NotNull
    @NotEmpty
    private String estado;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Cliente cliente;
}
