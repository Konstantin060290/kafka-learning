package factories;

import Configuration.KafkaOptions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaFactory {

    private KafkaOptions _kafkaOptions;
    public KafkaFactory(KafkaOptions kafkaOptions)
    {
        _kafkaOptions = kafkaOptions;
    }
    public KafkaProducer<String, String> getProducer()
    {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaOptions.connection.bootstrapServers); // Адреса брокеров Kafka
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, _kafkaOptions.producer.maxBlocksMs.toString()); // Макс. время ожидания метаданных
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, _kafkaOptions.producer.metaDataMaxAge.toString()); // Частота обновления метаданных (мс)

        // Настройки для At Least Once
        properties.put(ProducerConfig.ACKS_CONFIG, _kafkaOptions.producer.acks); // Ждём подтверждения от всех реплик
        properties.put(ProducerConfig.RETRIES_CONFIG, _kafkaOptions.producer.retries.toString()); // Бесконечные попытки повтора
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, _kafkaOptions.producer.maxInFlightRequestsPerConnection.toString()); // Запрещаем переупорядочивание
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, _kafkaOptions.producer.enableIdempotence); // Идемпотентность

        var producer = new KafkaProducer<String, String>(properties);

        return producer;
    }

    public KafkaConsumer<String, String> getConsumer(String topicName)
    {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaOptions.connection.bootstrapServers);  // Адреса брокеров Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, _kafkaOptions.consumer.groupId);        // Уникальный идентификатор группы
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, _kafkaOptions.consumer.autoOffsetReset);        // Начало чтения с самого начала
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, _kafkaOptions.consumer.enableAutoCommit);           // Автоматический коммит смещений
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, _kafkaOptions.consumer.sessionTimeOut.toString());           // Время ожидания активности от консьюмера

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList(topicName));

        return consumer;
    }
}
