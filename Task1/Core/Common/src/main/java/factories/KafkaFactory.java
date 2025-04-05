package factories;

import configuration.KafkaOptions;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaFactory {

    public KafkaProducer<String, String> getProducer()
    {
        var kafkaOptions = new KafkaOptions();

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaOptions.Connection.BootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaOptions.Producer.keySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaOptions.Producer.valueSerializer);
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaOptions.Producer.maxBlocksMs.toString()); // Макс. время ожидания метаданных
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, kafkaOptions.Producer.metaDataMaxAge.toString()); // Частота обновления метаданных (мс)

        // Настройки для At Least Once
        properties.put(ProducerConfig.ACKS_CONFIG, kafkaOptions.Producer.acks); // Ждём подтверждения от всех реплик
        properties.put(ProducerConfig.RETRIES_CONFIG, kafkaOptions.Producer.retries.toString()); // Бесконечные попытки повтора
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaOptions.Producer.maxInFlightRequestsPerConnection.toString()); // Запрещаем переупорядочивание
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaOptions.Producer.enableIdempotence); // Идемпотентность

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
