package com.maolabs.maobank.model;

import com.maolabs.maobank.model.exception.TransicaoDeStatusNaoSuportadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class PropostaTest {

    Proposta proposta;

    @BeforeEach
    void setUp() {
        proposta = new Proposta();
    }

    @Test
    void iniciar() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(false));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.NOVA));
    }

    @Test
    void concluirDadosBasicos() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(false));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.DADOS_INICIAIS_INFORMADOS));
    }

    @Test
    void concluirDadosEndereco() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        proposta.concluirDadosEndereco();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(false));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.ENDERECO_INFORMADO));
    }

    @Test
    void enviarParaAceite() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        proposta.concluirDadosEndereco();
        proposta.enviarParaAceite();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(false));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.AGUARDANDO_ACEITE));
    }

    @Test
    void aceitar() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        proposta.concluirDadosEndereco();
        proposta.enviarParaAceite();
        proposta.aceitar();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(true));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.ACEITE_POSITIVO));
    }

    @Test
    void rejeitar() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        proposta.concluirDadosEndereco();
        proposta.enviarParaAceite();
        proposta.rejeitar();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(true));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.ACEITE_NEGATIVO));
    }

    @Test
    void naoLiberar() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        proposta.concluirDadosEndereco();
        proposta.enviarParaAceite();
        proposta.aceitar();
        proposta.naoLiberar();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(true));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.NAO_LIBERADA));
    }

    @Test
    void liberar() throws TransicaoDeStatusNaoSuportadaException {
        proposta.iniciar();
        proposta.concluirDadosBasicos();
        proposta.concluirDadosEndereco();
        proposta.enviarParaAceite();
        proposta.aceitar();
        proposta.liberar();
        boolean propostaFinalizada = proposta.propostaFinalizada();

        assertThat(propostaFinalizada, equalTo(true));
        assertThat(proposta.getPropostaStatus(), equalTo(PropostaStatusEnum.LIBERADA));
    }
}