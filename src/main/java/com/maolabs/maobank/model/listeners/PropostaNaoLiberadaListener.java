package com.maolabs.maobank.model.listeners;

import com.maolabs.maobank.model.events.EmailEvent;
import com.maolabs.maobank.model.events.PropostaNaoLiberadaEvent;
import com.maolabs.maobank.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PropostaNaoLiberadaListener {

    @Autowired
    private ApplicationEventPublisher publisher;

    @TransactionalEventListener
    public void onApplicationEvent(PropostaNaoLiberadaEvent event) {

        var mensagem = EmailService.Mensagem.builder()
                .destinatario(event.getProposta().getCliente().getEmail())
                .assunto("Maobank - Atenção!")
                .nomeArquivoTemplate("proposta-inicial-nao-liberada.html")
                .modelo("proposta", event.getProposta())
                .build();
        publisher.publishEvent(new EmailEvent(mensagem));

    }

}
