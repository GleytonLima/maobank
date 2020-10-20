package com.maolabs.maobank.transferencia;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Conta;
import com.maolabs.maobank.model.Movimentacao;
import com.maolabs.maobank.service.ContaService;
import com.maolabs.maobank.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransferenciaConsumerTest {

    TransferenciaRecebidaRecord transferenciaRecebidaRecord;

    @InjectMocks
    TransferenciaConsumer transferenciaConsumer;

    @Mock
    ContaService contaService;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp() {
        transferenciaRecebidaRecord = new TransferenciaRecebidaRecord();

        Cliente cliente = new Cliente();
        cliente.setEmail("teste@teste.com");

        Conta conta = new Conta();
        conta.setCliente(cliente);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setValor(BigDecimal.valueOf(10.00));
        movimentacao.setContaDestino(conta);

        Mockito.doReturn(Optional.of(movimentacao)).when(contaService).criarMovimentacaoContaTransferenciaRecebida(transferenciaRecebidaRecord);
    }

    @Test
    void consume() {
        transferenciaConsumer.consume(transferenciaRecebidaRecord);
    }
}