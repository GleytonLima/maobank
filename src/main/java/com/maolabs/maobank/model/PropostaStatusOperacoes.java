package com.maolabs.maobank.model;

import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import org.springframework.context.ApplicationEventPublisher;

public interface PropostaStatusOperacoes {
    PropostaStatusEnum iniciar(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum concluirDadosBasicos(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum concluirDadosEndereco(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum enviarParaAceite(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum aceitarProposta(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum rejeitarProposta(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum liberar(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    PropostaStatusEnum naoLiberar(Proposta proposta) throws TransicaoDeStatusNaoSuportadaException;

    void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher);

}
