package com.example;

import com.example.configuration.KafkaOptions;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StoreManager
{
    @Autowired
    KafkaOptions kafkaOptions;
    private KeyValueStore<String, String> blockedUsersStore;
    private KeyValueStore<String, String> prohibitedWordsStore;
    private final Object lock = new Object();

    public void initBlockedUsersStore(ProcessorContext context) {
        synchronized (lock) {
            if (this.blockedUsersStore == null) {
                this.blockedUsersStore = context.getStateStore(kafkaOptions.stream.blockedUsersStoreName);
                if (this.blockedUsersStore == null) {
                    throw new IllegalStateException("StateStore не найден: " +
                            kafkaOptions.stream.blockedUsersStoreName);
                }
            }
        }
    }

    public Optional<KeyValueStore<String, String>> getBlockedUsersStore() {
        synchronized (lock) {
            return Optional.ofNullable(this.blockedUsersStore);
        }
    }

    public void initProhibitedWordsStore(ProcessorContext context) {
        synchronized (lock) {
            if (this.prohibitedWordsStore == null) {
                this.prohibitedWordsStore = context.getStateStore(kafkaOptions.stream.prohibitedWordsStoreName);
                if (this.prohibitedWordsStore == null) {
                    throw new IllegalStateException("StateStore не найден: " +
                            kafkaOptions.stream.prohibitedWordsStoreName);
                }
            }
        }
    }

    public Optional<KeyValueStore<String, String>> getProhibitedWordsStore() {
        synchronized (lock) {
            return Optional.ofNullable(this.prohibitedWordsStore);
        }
    }
}
