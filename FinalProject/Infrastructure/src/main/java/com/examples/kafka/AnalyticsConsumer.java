package com.examples.kafka;

import com.example.CommonKafkaOptions;
import com.example.ProductContract;
import com.example.ProductSearch;
import com.example.ProductsConsumerOptions;
import com.example.models.Product;
import com.example.models.Recommendation;
import com.example.repositories.RecommendationsRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

@Component
public class AnalyticsConsumer {
    @Autowired
    CommonKafkaOptions kafkaOptions;

    @Autowired
    ProductsConsumerOptions productsConsumerOptions;

    @Autowired
    RecommendationsRepository recommendationsRepository;

    public void ConsumeRequests() {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.bootstrapServers);  // Адреса брокеров Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-1");        // Уникальный идентификатор группы
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
        consumer.subscribe(Collections.singletonList("products-recommendations"));

        while (true) {
            try {
                for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100L))) {

                    System.out.printf("Получено сообщение: key = %s, value = %s, partition = %d, offset = %d%n", record.key(), record.value(), record.partition(), record.offset());

                    ProductSearch productSearch = objectMapper.readValue(record.value(), ProductSearch.class);

                    var recommendation = new Recommendation();
                    recommendation.setName(productSearch.productName);

                    recommendationsRepository.save(recommendation);

                }
            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }
}
