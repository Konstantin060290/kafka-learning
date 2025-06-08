import com.example.CommonKafkaOptions;
import com.example.ProductsStreamOptions;
import com.example.TopicsOptions;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.kafka.common.serialization.Serdes;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.state.Stores;

import org.springframework.context.annotation.Bean;

import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.Properties;

@Component
public class ProductsStream {

    @Autowired
    CommonKafkaOptions kafkaOptions;

    @Autowired
    ProductsStreamOptions productsStreamOptions;

    @Autowired
    TopicsOptions topicsOptions;

    @Bean
    public Properties getStreamsProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.bootstrapServers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, productsStreamOptions.applicationId);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        return props;
    }

    @PostConstruct
    public void startStream() throws Exception {

        StreamsBuilderFactoryBean factory = getStreamsBuilderFactoryBean();

        StreamsBuilder builder = factory.getObject();

        // Создаем поток обработки
        builder.stream(topicsOptions.productsTopicName, Consumed.with(Serdes.String(), Serdes.String()))
                .transformValues(() -> new MessageReaderTransformer(),
                        productsStreamOptions.prohibitedProductsStoreName)
                .filter((key, value) -> value.length() > 0)
                .to(topicsOptions.filteredProductsTopicName); // Автоматическая отправка в топик отфильтрованных продуктов
    }

    @NotNull
    private StreamsBuilderFactoryBean getStreamsBuilderFactoryBean() {
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

    public class MessageReaderTransformer implements ValueTransformerWithKey<String, String, String> {
        private ProcessorContext context;

        @Override
        public void init(ProcessorContext context) {
            this.context = context;
        }

        @Override
        public String transform(String key, String value) {
            
            KeyValueStore<String, String> prohibitedProducts = context.getStateStore(productsStreamOptions.prohibitedProductsStoreName);

            // Проверяем, не заблокирован ли продукт
            try (KeyValueIterator<String, String> iterator = prohibitedProducts.all()) {

                while (iterator.hasNext()) {
                    KeyValue<String, String> product = iterator.next();

                    if (product.equals(value)) {
                        System.out.println("Product is prohibited: " + value);
                        return "";
                    }
                }
            }

            return value;
        }

        @Override
        public void close() {

        }
    }
}
