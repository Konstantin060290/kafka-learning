package com.example;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
public class TestConsumer {


    @PostConstruct
    public void init() {

        (new Thread(this::Consume)).start();
    }
    @Autowired
    Configuration configuration;

    public void Consume() {

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, configuration.consumerUser, configuration.consumerPwd);

        Consumer<String, String> consumer = getStringUserConsumer(jaasCfg);
        consumer.subscribe(Arrays.asList(new String[]{"test-topic2"}));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.key() + ":" + record.value());
            }
        }
    }

    @NotNull
    private Consumer<String, String> getStringUserConsumer(String jaasCfg) {
        String deserializer = StringDeserializer.class.getName();

        Properties props = new Properties();
        props.put("bootstrap.servers", configuration.bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("group.id", "demo");
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", deserializer);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config", jaasCfg);
        props.put("ssl.truststore.location", configuration.trustStoreLocation);
        props.put("ssl.truststore.password", configuration.trustStorePwd);

        Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        return consumer;
    }
}
