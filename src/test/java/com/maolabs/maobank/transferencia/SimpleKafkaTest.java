package com.maolabs.maobank.transferencia;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@EmbeddedKafka
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
/**
 * https://blog.mimacom.com/testing-apache-kafka-with-spring-boot/
 */
class SimpleKafkaTest {

    private static final String TOPIC = "transferencia-maobank";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    BlockingQueue<ConsumerRecord<String, TransferenciaRecebidaRecord>> records;

    KafkaMessageListenerContainer<String, TransferenciaRecebidaRecord> container;

    @BeforeAll
    void setUp() {
        var configs = new HashMap<>(KafkaTestUtils.consumerProps("transferencia-group-id", "false", embeddedKafkaBroker));
        var consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new JsonDeserializer<>(TransferenciaRecebidaRecord.class));
        var containerProperties = new ContainerProperties(TOPIC);
        container = new KafkaMessageListenerContainer(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, TransferenciaRecebidaRecord>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

    @Test
    public void kafkaSetup_withTopic_ensureSendMessageIsReceived() throws Exception {
        // Produzindo
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        var producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>()).createProducer();
        TransferenciaRecebidaRecord transferenciaRecebidaRecord = new TransferenciaRecebidaRecord();
        producer.send(new ProducerRecord(TOPIC, "transferencia-group-id", transferenciaRecebidaRecord));
        producer.flush();

        // Consumindo
        ConsumerRecord<String, TransferenciaRecebidaRecord> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);

        // Assert
        assertThat(singleRecord, notNullValue());
        assertThat(singleRecord.key(), equalTo("transferencia-group-id"));
        assertThat(singleRecord.topic(), equalTo(TOPIC));
        assertThat(singleRecord.value(), equalTo(transferenciaRecebidaRecord));
    }
}