package com.maolabs.maobank.model.listeners;

import com.maolabs.maobank.model.Conta;
import com.maolabs.maobank.model.ContaRedefinirSenhaToken;
import com.maolabs.maobank.model.events.ContaNovaEvent;
import com.maolabs.maobank.model.events.EmailEvent;
import com.maolabs.maobank.service.ContaService;
import com.maolabs.maobank.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ContaNovaListener {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ContaService contaService;

    @EventListener
    public void onApplicationEvent(ContaNovaEvent event) {

        Conta conta = contaService.criarConta(event.getProposta());
        ContaRedefinirSenhaToken token = contaService.criarTokenRedefinirSenhaParaConta(conta);

        var mensagem = EmailService.Mensagem.builder().assunto("MaoBank - Tudo certo!")
                .nomeArquivoTemplate("conta-nova-liberada.html")
                .destinatario(event.getProposta().getCliente().getEmail())
                .modelo("token", token.getToken())
                .modelo("conta", conta)
                .modelo("proposta", event.getProposta())
                .build();

        publisher.publishEvent(new EmailEvent(mensagem));
    }
}
