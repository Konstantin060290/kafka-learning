package com.example;

import com.example.configuration.KafkaOptions;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaFactory {
    @Autowired
    KafkaOptions _kafkaOptions;
    @Bean
    @Scope("prototype")
    public KafkaProducer<String, String> getProducer()
    {
        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaOptions.connection.bootstrapServers); // Адреса брокеров Kafka
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, _kafkaOptions.producer.maxBlocksMs.toString()); // Макс. время ожидания метаданных
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, _kafkaOptions.producer.metaDataMaxAge.toString()); // Частота обновления метаданных (мс)

        // Настройки для At Least Once
        properties.put(ProducerConfig.ACKS_CONFIG, _kafkaOptions.producer.acks); // Ждём подтверждения от всех реплик
        properties.put(ProducerConfig.RETRIES_CONFIG, _kafkaOptions.producer.retries.toString()); // Количество попыток повтора
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, _kafkaOptions.producer.maxInFlightRequestsPerConnection.toString()); // Запрещаем переупорядочивание
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, _kafkaOptions.producer.enableIdempotence); // Идемпотентность

        var producer = new KafkaProducer<String, String>(properties);

        return producer;
    }
}
