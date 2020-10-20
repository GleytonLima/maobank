package com.maolabs.maobank.transferencia;

import com.maolabs.maobank.model.Movimentacao;
import com.maolabs.maobank.service.ContaService;
import com.maolabs.maobank.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransferenciaConsumer {

    Logger logger = LoggerFactory.getLogger(TransferenciaConsumer.class);

    @Autowired
    private ContaService contaService;

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "transferencias-para-maobank", groupId = "transferencia-group-id", containerFactory = "transferenciaKafkaListenerContainerFactory")
    void consume(TransferenciaRecebidaRecord transferencia) {
        logger.info("Consumindo Transferencia...");

        var movimentacaoConta = contaService.criarMovimentacaoContaTransferenciaRecebida(transferencia);

        if (movimentacaoConta.isPresent()) {

            var mensagem = EmailService.Mensagem.builder()
                    .destinatario(movimentacaoConta.get().getContaDestino().getCliente().getEmail())
                    .assunto("Transferência Recebida!")
                    .nomeArquivoTemplate("transferencia-recebida.html")
                    .modelo("movimentacao", movimentacaoConta.get())
                    .modelo("cliente", movimentacaoConta.get().getContaDestino().getCliente())
                    .build();
            emailService.sendSimpleMessage(mensagem);

        }
    }

}
