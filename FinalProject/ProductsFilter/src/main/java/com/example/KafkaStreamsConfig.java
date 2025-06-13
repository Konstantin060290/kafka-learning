package com.example;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
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
    CommonKafkaOptions kafkaOptions;

    @Autowired
    ProductsStreamOptions productsStreamOptions;

    @Bean
    public Properties getStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.bootstrapServers);

        // Конфигурация SASL
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaOptions.securityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, kafkaOptions.saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG,

                String.format("org.apache.kafka.common.security.plain.PlainLoginModule required " +
                        "username=\"%s\" password=\"%s\";", "admin", "admin-secret"));

        props.put("ssl.truststore.location", "D:\\kafka\\files\\kafka.truststore.jks");
        props.put("ssl.truststore.password", "123456");

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, productsStreamOptions.applicationId);
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
                        Stores.persistentKeyValueStore(productsStreamOptions.prohibitedProductsStoreName),
                        Serdes.String(),
                        Serdes.String()
                ));
            }
        });

        return factory;
    }
}