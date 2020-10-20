package com.maolabs.maobank.transferencia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaProducer {

    Logger logger = LoggerFactory.getLogger(TransferenciaProducer.class);

    @Value(value = "${transferencia.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, TransferenciaRecebidaRecord> kafkaTemplate;

    public void criarTransferencia(TransferenciaRecebidaRecord transferenciaRecebidaRecord) {
        logger.info("Produzindo");
        this.kafkaTemplate.send(topicName, transferenciaRecebidaRecord);
    }
}
