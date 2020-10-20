package com.maolabs.maobank.transferencia;

import com.maolabs.maobank.model.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TransferenciaRecebidaRecord {
    private BigDecimal valor;
    private LocalDate dataRealizacao;
    private String documentoIdentificadorOrigem;
    private String bancoOrigem;
    private String contaOrigem;
    private String agenciaOrigem;
    private String codigoUnico;
    private String contaDestino;
    private String agenciaDestino;

    public Movimentacao buildMovimentacaoConta(Conta contaDestino) {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setValor(valor);
        movimentacao.setTipoValorEnum(MovimentacaoValorTipoEnum.CREDITO);
        movimentacao.setTipo(MovimentacaoTipoEnum.TRANSFERENCIA_RECEBIDA);
        movimentacao.setCodigoUnico(codigoUnico);
        movimentacao.setContaDestino(contaDestino);
        return movimentacao;
    }

    public TransferenciaRecebida buildTransferenciaRecebida(Movimentacao movimentacao) {
        TransferenciaRecebida transferenciaRecebida = new TransferenciaRecebida();
        transferenciaRecebida.setValor(valor);
        transferenciaRecebida.setDataRealizacao(dataRealizacao);
        transferenciaRecebida.setDocumentoIdentificadorOrigem(documentoIdentificadorOrigem);
        transferenciaRecebida.setBancoOrigem(bancoOrigem);
        transferenciaRecebida.setContaOrigem(contaOrigem);
        transferenciaRecebida.setAgenciaOrigem(agenciaOrigem);
        transferenciaRecebida.setCodigoUnico(codigoUnico);
        transferenciaRecebida.setContaDestino(contaDestino);
        transferenciaRecebida.setAgenciaDestino(agenciaDestino);
        transferenciaRecebida.setMovimentacao(movimentacao);
        return transferenciaRecebida;
    }

}