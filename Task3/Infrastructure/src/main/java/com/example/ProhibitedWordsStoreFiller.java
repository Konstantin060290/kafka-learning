package com.example;

import com.example.configuration.KafkaOptions;
import com.example.configuration.KafkaStreamsConfig;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.StreamsBuilder;
import java.util.Properties;

@Component
public class ProhibitedWordsStoreFiller {
    @Autowired
    KafkaStreamsConfig streamProperties;
    @Autowired
    KafkaOptions kafkaOptions;

    @PostConstruct
    public void FillStore() {
        try {
            // Конфигурация Kafka Streams
            Properties properties = new Properties();
            properties.putAll(streamProperties.getStreamsProperties());
            properties.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaOptions.stream.applicationId + "-prohibited-words");

            // Создаём топологию
            StreamsBuilder builder = new StreamsBuilder();

            //Инициализируем state store
            builder.addStateStore(Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(kafkaOptions.stream.prohibitedWordsStoreName), Serdes.String(), // Key serde
                    Serdes.String()    // Value serde
            ));

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

            // Старт потока
            KafkaStreams streams = new KafkaStreams(builder.build(), properties);
            streams.start();

        } catch (Exception e) {
            System.err.println("Ошибка при запуске Kafka Streams приложения: " + e.getMessage());
        }
    }
}
