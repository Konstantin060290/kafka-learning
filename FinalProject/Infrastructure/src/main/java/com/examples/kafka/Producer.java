package com.examples.kafka;

import com.example.ClientApiOptions;
import com.example.CommonKafkaOptions;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class Producer {

    @Autowired
    ClientApiOptions configuration;

    @Autowired
    CommonKafkaOptions kafkaOptions;

    public void Produce(String message, String topicName) {

        try {

            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.bootstrapServers); // Адреса брокеров Kafka
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, configuration.maxBlocksMs.toString()); // Макс. время ожидания метаданных
            properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, configuration.metaDataMaxAge.toString()); // Частота обновления метаданных (мс)

            // Настройки для At Least Once
            properties.put(ProducerConfig.ACKS_CONFIG, configuration.acks); // Ждём подтверждения от всех реплик
            properties.put(ProducerConfig.RETRIES_CONFIG, configuration.retries.toString()); // Количество попыток повтора
            properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, configuration.maxInFlightRequestsPerConnection.toString()); // Запрещаем переупорядочивание
            properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, configuration.enableIdempotence); // Идемпотентность

            // Конфигурация SASL
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaOptions.securityProtocol);
            properties.put(SaslConfigs.SASL_MECHANISM, kafkaOptions.saslMechanism);
            properties.put(SaslConfigs.SASL_JAAS_CONFIG,

                    String.format("org.apache.kafka.common.security.plain.PlainLoginModule required " +
                            "username=\"%s\" password=\"%s\";", configuration.producerUser, configuration.producerPwd));

            properties.put("ssl.truststore.location", "D:\\kafka\\files\\kafka.truststore.jks");
            properties.put("ssl.truststore.password", "123456");

            var producer = new KafkaProducer<String, String>(properties);

            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, Uuid.randomUuid().toString(), message);

            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Delivery failed: " + exception.getMessage());
                    exception.printStackTrace(System.err);
                } else {
                    System.out.printf("Delivered message %s to topic %s [%d] at offset %d%n",
                            record, metadata.topic(), metadata.partition(), metadata.offset());
                }
            });

            producer.flush();
            producer.close();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
