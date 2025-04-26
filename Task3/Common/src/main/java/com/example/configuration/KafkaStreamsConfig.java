package com.example.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {
    @Autowired
    KafkaOptions kafkaOptions;

    @Bean
    public Properties getStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.connection.bootstrapServers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaOptions.stream.applicationId);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        return props;
    }

    @Bean
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean() {

        Properties props = getStreamsProperties();

        StreamsBuilderFactoryBean factory = new StreamsBuilderFactoryBean();
        factory.setStreamsConfiguration(props);
        factory.setCloseTimeout(10);

        // Конфигурация StateStore через InfrastructureCustomizer
        factory.setInfrastructureCustomizer(new KafkaStreamsInfrastructureCustomizer() {
            @Override
            public void configureBuilder(StreamsBuilder builder) {
                builder.addStateStore(Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(kafkaOptions.stream.prohibitedWordsStoreName),
                        Serdes.String(),
                        Serdes.String()
                ));

                builder.addStateStore(Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore(kafkaOptions.stream.blockedUsersStoreName),
                        Serdes.String(),
                        Serdes.String()
                ));
            }
        });

        return factory;
    }
}
