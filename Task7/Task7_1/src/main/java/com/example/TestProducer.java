package com.example;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import jakarta.annotation.PostConstruct;
import org.apache.avro.data.Json;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TestProducer {

    @PostConstruct
    public void init() {
        (new Thread(this::ProduceSomething)).start();
    }
    
    @Autowired
    Configuration configuration;

    public void ProduceSomething() {

        try {

            String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
            String jaasCfg = String.format(jaasTemplate, configuration.producerUser, configuration.producerPwd);

            String topic = "test-topic";

            KafkaProducer<String, User> producer = getStringUserKafkaProducer(jaasCfg);

            User user = new User("First user", 42, "blue", "swimming");
            ProducerRecord<String, User> record = new ProducerRecord<>(topic, "first-user", user);
            record.headers().add(new RecordHeader("myTestHeader", "header values are binary".getBytes()));

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Delivery failed: " + exception.getMessage());
                    exception.printStackTrace(System.err);
                } else {
                    System.out.printf("Delivered message %s to topic %s [%d] at offset %d%n",
                    user.toString(), metadata.topic(), metadata.partition(), metadata.offset());
                }
            });

            producer.flush();
            producer.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @NotNull
    private KafkaProducer<String, User> getStringUserKafkaProducer(String jaasCfg) {
        Properties props = new Properties();
        props.put("bootstrap.servers", configuration.bootstrapServers);
        props.put("acks", "all");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config", jaasCfg);
        props.put("ssl.truststore.location", configuration.trustStoreLocation);
        props.put("ssl.truststore.password", configuration.trustStorePwd);
        props.put("ssl.endpoint.identification.algorithm", "https");

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class.getName());

        // Настройки Schema Registry
        String schemaRegistryUrl = configuration.schemaRegistryUrl;
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put("schema.registry.basic.auth.credentials.source", "USER_INFO");
        props.put("schema.registry.basic.auth.user.info", configuration.producerUser + ":" + configuration.producerPwd);
        props.put("schema.registry.ssl.truststore.location", configuration.caLocation);
        props.put("schema.registry.ssl.truststore.type", "PEM");
        props.put("auto.register.schemas", "true");
        props.put("use.latest.version", "true");

        KafkaProducer<String, User> producer = new KafkaProducer<>(props);
        return producer;
    }

}
