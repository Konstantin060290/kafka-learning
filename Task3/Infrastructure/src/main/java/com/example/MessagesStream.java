package com.example;

import com.example.configuration.KafkaOptions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessagesStream {
    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @Autowired
    KafkaOptions kafkaOptions;
    @Autowired
    BadWordsReplacer badWordsReplacer;

    @PostConstruct
    public void startStream() throws Exception {
        // Получаем топологию
        StreamsBuilder builder = streamsBuilderFactoryBean.getObject();

        // Создаем поток обработки
        builder.stream(kafkaOptions.stream.messagesTopicName, Consumed.with(Serdes.String(), Serdes.String()))
                .transformValues(() -> new MessageReaderTransformer(),
                        kafkaOptions.stream.blockedUsersStoreName,
                        kafkaOptions.stream.prohibitedWordsStoreName)
                .to(kafkaOptions.stream.filteredMessagesTopicName); // Автоматическая отправка в топик

        // Дополнительная обработка
        builder.stream(kafkaOptions.stream.messagesTopicName)
                .foreach((key, value) -> {
                    additionalProcessMessage((String)key, (String)value);
                });
    }

    private void additionalProcessMessage(String key, String value) {
        // Запишем в консоль оригинальное сообщение
        System.out.println("Received message - Key: " + key + ", Value: " + value);
    }

    @PreDestroy
    public void stop() {
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

