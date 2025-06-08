package com.example;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.kafka.common.serialization.Serdes;

import org.springframework.kafka.config.StreamsBuilderFactoryBean;

@Component
public class ProductsStream {
    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    private ProductsStreamOptions productsStreamOptions;

    @Autowired
    private TopicsOptions topicsOptions;

    @PostConstruct
    public void startStream() throws Exception {

        // Получаем топологию
        StreamsBuilder builder = streamsBuilderFactoryBean.getObject();

        builder.stream(topicsOptions.productsTopicName,
                        Consumed.with(Serdes.String(), Serdes.String()))
                .transformValues(() -> new MessageReaderTransformer(),
                        productsStreamOptions.prohibitedProductsStoreName)
                .filter((key, value) -> value.length() > 0)
                .to(topicsOptions.filteredProductsTopicName);
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

                    if (product.value.equals(value)) {
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
