package com.example;

import com.example.configuration.KafkaOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    @Autowired
    StoreManager storeManager;

    @PostConstruct
    public void startStream() throws Exception {
        // Получаем топологию
        StreamsBuilder builder = streamsBuilderFactoryBean.getObject();

        // Создаем поток обработки
        builder.stream(kafkaOptions.stream.messagesTopicName, Consumed.with(Serdes.String(), Serdes.String()))
                .transformValues(() -> new MessageReaderTransformer(),
                        kafkaOptions.stream.blockedUsersStoreName,
                        kafkaOptions.stream.prohibitedWordsStoreName)
                .filter((key, value) -> value.length() > 0)
                .to(kafkaOptions.stream.filteredMessagesTopicName); // Автоматическая отправка в топик

        // Дополнительная обработка
        builder.stream(kafkaOptions.stream.messagesTopicName)
                .foreach((key, value) -> {
                    additionalProcessMessage((String) key, (String) value);
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
    public class MessageReaderTransformer implements ValueTransformerWithKey<String, String, String> {

        @Override
        public void init(ProcessorContext context) {
            storeManager.initBlockedUsersStore(context);
        }

        @Override
        public String transform(String key, String value) {

            // Десериализуем входящее сообщение
            Message message = deserializeMessage(value);
            if (message == null) {
                return "";
            }

            var blockedUsersStore = storeManager.getBlockedUsersStore();
            var prohibitedWordsStore = storeManager.getProhibitedWordsStore();

            // Проверяем, не заблокирован ли отправитель
            blockedUsersStore.ifPresent(store -> {
                try (KeyValueIterator<String, String> iterator = store.all()) {

                    while (iterator.hasNext()) {
                        KeyValue<String, String> user = iterator.next();
                        BlockedUser blockedUser = deserializeBlockedUser(user.value);

                        if (blockedUser == null) {
                            throw new IllegalStateException("Failed deserialize user");
                        }

                        // Проверяем, совпадает ли отправитель с заблокированным пользователем
                        if (blockedUser.Blocked.Name.equals(message.from)) {
                            System.out.println("Message from blocked user: " + message.from);
                            throw new IllegalStateException("Message from blocked user: " + message.from);
                        }
                    }
                }
            });

            List<String> allProhibitedWords = new ArrayList<>();

            prohibitedWordsStore.ifPresent(store -> {
                try (KeyValueIterator<String, String> iterator = store.all()) {
                    while (iterator.hasNext()) {
                        KeyValue<String, String> entry = iterator.next();

                        var word = deserializeWord(entry.value);

                        if (word == null) {
                            throw new IllegalStateException("Failed deserialize word");
                        }

                        allProhibitedWords.add(word.Word);
                        System.out.println("Found prohibited word: " + entry.value);
                    }
                }
            });

            var transformedBody = badWordsReplacer.Replace(message.body, allProhibitedWords);
            message.body = transformedBody;

            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());

            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                return "";
            }

            return jsonString;

        }

        @Override
        public void close() {

        }

        private Message deserializeMessage(String value) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(value, Message.class);
            } catch (Exception e) {
                System.err.println("Failed to deserialize message: " + e.getMessage());
                return null;
            }
        }

        private BlockedUser deserializeBlockedUser(String value) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(value, BlockedUser.class);
            } catch (Exception e) {
                System.err.println("Failed to deserialize message: " + e.getMessage());
                return null;
            }
        }

        private ProhibitedWord deserializeWord(String value) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(value, ProhibitedWord.class);
            } catch (Exception e) {
                System.err.println("Failed to deserialize message: " + e.getMessage());
                return null;
            }
        }
    }
}

