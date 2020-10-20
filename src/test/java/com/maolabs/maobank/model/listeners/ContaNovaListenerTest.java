package com.maolabs.maobank.model.listeners;

import com.maolabs.maobank.model.Cliente;
import com.maolabs.maobank.model.Conta;
import com.maolabs.maobank.model.ContaRedefinirSenhaToken;
import com.maolabs.maobank.model.Proposta;
import com.maolabs.maobank.model.events.ContaNovaEvent;
import com.maolabs.maobank.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContaNovaListenerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @MockBean
    private ContaService contaService;

    Proposta proposta;
    Conta conta;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Joao Teste");
        cliente.setEmail("teste@email.com");

        conta = new Conta();
        conta.setId(1L);

        ContaRedefinirSenhaToken contaRedefinirSenhaToken = new ContaRedefinirSenhaToken(conta);

        proposta = new Proposta();
        proposta.setId(1L);
        proposta.setCliente(cliente);

        Mockito.when(contaService.criarConta(proposta)).thenReturn(conta);
        Mockito.when(contaService.criarTokenRedefinirSenhaParaConta(conta)).thenReturn(contaRedefinirSenhaToken);
    }

    @Test
    void onApplicationEvent() {
        publisher.publishEvent(new ContaNovaEvent(proposta));
        verify(contaService, times(1)).criarConta(proposta);
        verify(contaService, times(1)).criarTokenRedefinirSenhaParaConta(conta);
    }
}