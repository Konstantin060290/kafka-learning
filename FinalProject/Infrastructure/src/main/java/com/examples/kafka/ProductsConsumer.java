package com.examples.kafka;

import com.example.CommonKafkaOptions;
import com.example.ProductContract;
import com.example.ProductsConsumerOptions;
import com.example.models.Product;
import com.example.repositories.ProductsRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Component
public class ProductsConsumer {
    @Autowired
    CommonKafkaOptions kafkaOptions;

    @Autowired
    ProductsConsumerOptions productsConsumerOptions;

    @Autowired
    ProductsRepository productsRepository;

    @Transactional
    @Async
    @PostConstruct
    public void ConsumeProducts() {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.bootstrapServers);  // Адреса брокеров Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, productsConsumerOptions.consumerGroupId);        // Уникальный идентификатор группы
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, productsConsumerOptions.autoOffsetReset);        // Начало чтения с самого начала
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, productsConsumerOptions.enableAutoCommit);           // Автоматический коммит смещений
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, productsConsumerOptions.sessionTimeOut.toString());           // Время ожидания активности от консьюмера

        // Конфигурация SASL
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaOptions.securityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, kafkaOptions.saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG,

                String.format("org.apache.kafka.common.security.plain.PlainLoginModule required " +
                        "username=\"%s\" password=\"%s\";", productsConsumerOptions.consumerLogin, productsConsumerOptions.consumerPwd));

        props.put("ssl.truststore.location", kafkaOptions.trustStoreLocation);
        props.put("ssl.truststore.password", kafkaOptions.trustStorePassword);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // для поддержки Instant
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList("filtered-products"));

        while (true)
        {
            try {
                for(ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100L))) {

                    ProductContract productContract = objectMapper.readValue(record.value(), ProductContract.class);

                    var product = new Product();
                    product.setProductId(productContract.getProductId());
                    product.setName(productContract.getName());
                    product.setRestInformation(record.value());

                    productsRepository.save(product);

                    System.out.printf("Получено сообщение: key = %s, value = %s, partition = %d, offset = %d%n", record.key(), record.value(), record.partition(), record.offset());
                }
            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }
}
