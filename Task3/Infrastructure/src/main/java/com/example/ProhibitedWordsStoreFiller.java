package com.example;

import com.example.configuration.KafkaOptions;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.StreamsBuilder;

@Component
public class ProhibitedWordsStoreFiller {
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
            KStream<String, String> stream = builder.stream(kafkaOptions.stream.prohibitedWordsTopicName, Consumed.with(Serdes.String(), Serdes.String()));


            // Обработка сообщений с доступом к State Store
            stream.process(() -> new Processor<>() {
                private KeyValueStore<String, String> store;
                @Override
                public void init(ProcessorContext context) {
                    this.store = context.getStateStore(kafkaOptions.stream.prohibitedWordsStoreName);
                }

                @Override
                public void process(String key, String value) {
                    // Сохраняем сообщение в State Store
                    store.put(key, value);
                    System.out.printf("В store добавлено запрещенное слово: %s", key);
                }

                @Override
                public void close() {
                }
            }, kafkaOptions.stream.prohibitedWordsStoreName);

        } catch (Exception e) {
            System.err.println("Ошибка при запуске Kafka Streams приложения: " + e.getMessage());
        }
    }
}
