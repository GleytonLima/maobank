package com.maolabs.maobank.model.listeners;

import com.maolabs.maobank.model.events.EmailEvent;
import com.maolabs.maobank.model.events.PropostaAceitaEvent;
import com.maolabs.maobank.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PropostaAceitaListener {

    @Autowired
    private ApplicationEventPublisher publisher;

    @TransactionalEventListener
    public void onApplicationEvent(PropostaAceitaEvent event) {
        var mensagem = EmailService.Mensagem.builder()
                .destinatario(event.getProposta().getCliente().getEmail())
                .assunto("MaoBank - Deu match!")
                .nomeArquivoTemplate("boas-vindas-pos-aceite.html")
                .modelo("proposta", event.getProposta())
                .build();
        publisher.publishEvent(new EmailEvent(mensagem));
    }

}
