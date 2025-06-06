package com.example;

import com.example.configuration.KafkaOptions;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class BlockedUsersStoreFiller {
    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @Autowired
    KafkaOptions kafkaOptions;

    @PostConstruct
    public void FillStore() {
        try {
            // Получаем топологию
            StreamsBuilder builder = streamsBuilderFactoryBean.getObject();

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
                    System.out.printf("В store добавлена блокировка пользователя: %s\n", value);
                }

                @Override
                public void close() {
                }
            }, kafkaOptions.stream.blockedUsersStoreName);

        } catch (Exception e) {
            System.err.println("Ошибка при запуске Kafka Streams приложения: " + e.getMessage());
        }
    }
}
