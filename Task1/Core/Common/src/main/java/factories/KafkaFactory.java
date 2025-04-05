package factories;

import Configuration.KafkaOptions;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

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
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, _kafkaOptions.connection.bootstrapServers);
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

    public KafkaConsumer<String, String> getConsumer()
    {
        var kafkaOptions = new KafkaOptions();

        Properties properties = new Properties();

        var consumer = new KafkaConsumer<String, String>(properties);

        return consumer;
    }
}
