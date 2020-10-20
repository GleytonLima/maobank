package com.maolabs.maobank.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Audited
@AuditTable(value="documento_AUDITORIA")
public class Documento extends AbstractAuditingEntity {
    @Id
    private Long id;

    @NotEmpty
    @NotNull
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Cliente cliente;

    public Documento(String url, Cliente cliente) {
        this.url = url;
        this.cliente = cliente;
    }
}
