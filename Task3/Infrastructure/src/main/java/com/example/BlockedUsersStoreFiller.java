package com.example;

import com.example.configuration.KafkaOptions;
import com.example.configuration.KafkaStreamsConfig;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlockedUsersStoreFiller {
    @Autowired
    KafkaStreamsConfig streamsConfig;
    @Autowired
    KafkaOptions kafkaOptions;

    @PostConstruct
    public void FillStore() {
        try {
            // Получаем топологию
            StreamsBuilder builder = streamsConfig.streamsBuilderFactoryBean().getObject();

            // Создаём KStream из топика с входными данными
            KStream<String, String> stream = builder.stream(kafkaOptions.stream.blockedUsersTopicName, Consumed.with(Serdes.String(), Serdes.String()));

            // Обработка сообщений с доступом к State Store
            stream.process(() -> new Processor<>() {
                private KeyValueStore<String, String> store;

                @Override
                public void init(ProcessorContext context) {
                    this.store = context.getStateStore(kafkaOptions.stream.blockedUsersStoreName);
                }

                @Override
                public void process(String key, String value) {
                    // Сохраняем сообщение в State Store
                    store.put(key, value);
                    System.out.printf("В store добавлено блокировка пользователя: %s", key);
                }

                @Override
                public void close() {
                }
            }, kafkaOptions.stream.blockedUsersStoreName);

            // 4. Строим топологию и запускаем
            KafkaStreams streams = new KafkaStreams(
                    builder.build(),
                    streamsConfig.getStreamsProperties()
            );

            streams.start();

        } catch (Exception e) {
            System.err.println("Ошибка при запуске Kafka Streams приложения: " + e.getMessage());
        }
    }
}
