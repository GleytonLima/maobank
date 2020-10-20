package com.maolabs.maobank.model;

import com.maolabs.maobank.model.events.ContaNovaEvent;
import com.maolabs.maobank.model.events.PropostaAceitaEvent;
import com.maolabs.maobank.model.events.PropostaNaoLiberadaEvent;
import com.maolabs.maobank.model.events.PropostaRejeitadaEvent;
import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Audited
@AuditTable(value = "proposta_AUDITORIA")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Proposta extends AbstractAggregateRoot<Proposta> {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PropostaStatusEnum propostaStatus;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    private LocalDateTime dataUltimaModificacao;

    public void iniciar() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.NOVA);
    }

    public boolean propostaFinalizada() {
        return propostaStatus != null && propostaStatus.finalizada();
    }

    public void concluirDadosBasicos() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.DADOS_INICIAIS_INFORMADOS);
    }

    public void concluirDadosEndereco() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.ENDERECO_INFORMADO);
    }

    public void aceitar() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.ACEITE_POSITIVO);
        registerEvent(new PropostaAceitaEvent(this));
    }

    public void rejeitar() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.ACEITE_NEGATIVO);
        registerEvent(new PropostaRejeitadaEvent(this));
    }

    public void naoLiberar() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.NAO_LIBERADA);
        registerEvent(new PropostaNaoLiberadaEvent(this));
    }

    public void enviarParaAceite() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.AGUARDANDO_ACEITE);
    }

    public void liberar() throws TransicaoDeStatusNaoSuportadaException {
        setPropostaStatus(PropostaStatusEnum.LIBERADA);
        registerEvent(new ContaNovaEvent(this));
    }

    private void setPropostaStatus(PropostaStatusEnum novoStatus) throws TransicaoDeStatusNaoSuportadaException {

        if (getPropostaStatus() != null && getPropostaStatus().naoPodeAlterarPara(novoStatus)) {
            throw new TransicaoDeStatusNaoSuportadaException(
                    String.format("Status do pedido não pode ser alterado de %s para %s",
                            getPropostaStatus().getDescricao(),
                            novoStatus.getDescricao()));
        }

        this.propostaStatus = novoStatus;
    }
}
