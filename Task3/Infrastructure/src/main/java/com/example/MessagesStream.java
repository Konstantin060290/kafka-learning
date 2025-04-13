package com.example;

import com.example.configuration.KafkaOptions;
import com.example.configuration.KafkaStreamsConfig;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessagesStream {
    @Autowired
    KafkaStreamsConfig streamProperties;
    @Autowired
    KafkaOptions kafkaOptions;
    @Autowired
    BadWordsReplacer badWordsReplacer;

    private KafkaStreams streams;

    @PostConstruct
    public void startStream() {
        Properties props = new Properties();
        props.putAll(streamProperties.getStreamsProperties());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaOptions.stream.applicationId + "-reader");

        StreamsBuilder builder = new StreamsBuilder();

        // Доступ к существующему StateStore (без повторного создания)
        builder.addStateStore(Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(kafkaOptions.stream.blockedUsersStoreName),
                Serdes.String(),
                Serdes.String()
        ).withLoggingDisabled()); // Отключаем логирование, если store уже существует

        builder.addStateStore(Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(kafkaOptions.stream.prohibitedWordsStoreName),
                Serdes.String(),
                Serdes.String()
        ).withLoggingDisabled());

        // Создаем поток обработки
        builder.stream(kafkaOptions.stream.messagesTopicName, Consumed.with(Serdes.String(), Serdes.String()))
                .transformValues(() -> new MessageReaderTransformer(),
                        kafkaOptions.stream.blockedUsersStoreName,
                        kafkaOptions.stream.prohibitedWordsStoreName)
                .foreach(this::processMessage);

        streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

    private void processMessage(String key, String value) {
        // Обработка очищенных сообщений
        System.out.println("Received message - Key: " + key + ", Value: " + value);
    }

    @PreDestroy
    public void stop() {
        if (streams != null) {
            streams.close();
        }
    }

    // Трансформер с доступом к StateStore
    private class MessageReaderTransformer implements ValueTransformerWithKey<String, String, String> {
        private KeyValueStore<String, String> blockedUsersStore;
        private KeyValueStore<String, String> prohibitedWordsStore;
        private ProcessorContext context;

        @Override
        public void init(ProcessorContext context) {
            this.context = context;
            this.blockedUsersStore = context.getStateStore(kafkaOptions.stream.blockedUsersStoreName);
            this.prohibitedWordsStore = context.getStateStore(kafkaOptions.stream.prohibitedWordsStoreName);
        }

        @Override
        public String transform(String key, String value) {
            // Проверяем, не заблокирован ли пользователь
            if (blockedUsersStore.get(key) != null) {
                return null; // Пропускаем сообщение
            }

            List<String> allProhibitedWords = new ArrayList<>();
            try (KeyValueIterator<String, String> iterator = prohibitedWordsStore.all()) {
                while (iterator.hasNext()) {
                    KeyValue<String, String> entry = iterator.next();
                    allProhibitedWords.add(entry.value);
                    System.out.println("Found prohibited word: " + entry.value); // Добавьте лог для отладки
                }
            }

            var transformedMessage = badWordsReplacer.Replace(value, allProhibitedWords);

            return transformedMessage;
        }

        @Override
        public void close() {

        }
    }
}

