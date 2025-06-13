package com.example;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ProhibitedProductsStoreFiller {

    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    private ProductsStreamOptions productsStreamOptions;

    @Autowired
    TopicsOptions topicsOptions;

    @PostConstruct
    public void FillStore() {
        try {
            // Получаем топологию
            StreamsBuilder builder = streamsBuilderFactoryBean.getObject();

            // Создаём KStream из топика с входными данными
            KStream<String, String> stream = builder.stream(topicsOptions.blockedProductsTopicName, Consumed.with(Serdes.String(), Serdes.String()));

            // Обработка сообщений с доступом к State Store
            stream.process(() -> new Processor<>() {
                private KeyValueStore<String, String> store;

                @Override
                public void init(org.apache.kafka.streams.processor.api.ProcessorContext<Object, Object> context) {
                    this.store = context.getStateStore(productsStreamOptions.prohibitedProductsStoreName);
                }

                @Override
                public void process(Record<String, String> record) {
                    // Сохраняем сообщение в State Store
                    store.put(record.key(), record.value());
                    System.out.printf("В store добавлена блокировка продукта: %s\n", record.value());
                }

                @Override
                public void close() {
                }
            }, productsStreamOptions.prohibitedProductsStoreName);

        } catch (Exception e) {
            System.err.println("Ошибка при запуске Kafka Streams приложения: " + e.getMessage());
        }
    }
}