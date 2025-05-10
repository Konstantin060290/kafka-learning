package factories;
import com.example.configuration.KafkaOptions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

@Component
public class KafkaConsumerFactory {
    @Autowired
    private KafkaOptions _kafkaOptions;
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
