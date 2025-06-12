package com.examples.kafka;

import com.example.CommonKafkaOptions;
import com.example.ProductsConsumerOptions;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

@Component
public class RequestsConsumer {
    @Autowired
    CommonKafkaOptions kafkaOptions;

    @Autowired
    ProductsConsumerOptions productsConsumerOptions;

    @PostConstruct
    public void ConsumeRequests() {

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaOptions.bootstrapServers);  // Адреса брокеров Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-1");        // Уникальный идентификатор группы
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, productsConsumerOptions.autoOffsetReset);        // Начало чтения с самого начала
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, productsConsumerOptions.enableAutoCommit);           // Автоматический коммит смещений
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, productsConsumerOptions.sessionTimeOut.toString());           // Время ожидания активности от консьюмера

        // Конфигурация SASL
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaOptions.securityProtocol);
        props.put(SaslConfigs.SASL_MECHANISM, kafkaOptions.saslMechanism);
        props.put(SaslConfigs.SASL_JAAS_CONFIG,

                String.format("org.apache.kafka.common.security.plain.PlainLoginModule required " +
                        "username=\"%s\" password=\"%s\";", productsConsumerOptions.consumerLogin, productsConsumerOptions.consumerPwd));

        props.put("ssl.truststore.location", kafkaOptions.trustStoreLocation);
        props.put("ssl.truststore.password", kafkaOptions.trustStorePassword);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList("source.users-search-requests"));

        String hdfsUri = "hdfs://hadoop-namenode:9000";
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);
        conf.set("dfs.replication", "1");
        conf.set("dfs.client.use.datanode.hostname", "true");
        conf.set("dfs.datanode.use.datanode.hostname", "false");
        conf.set("hadoop.security.authentication", "simple");


        try (FileSystem hdfs = FileSystem.get(new URI(hdfsUri), conf, "root")) {
            while (true) {
                try {
                    for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100L))) {

                        System.out.printf("Получено сообщение: key = %s, value = %s, partition = %d, offset = %d%n", record.key(), record.value(), record.partition(), record.offset());

                        String hdfsFilePath = "/user-requests/message_" + UUID.randomUUID();
                        Path path = new Path(hdfsFilePath);

                        // Запись файла в HDFS
                        try (FSDataOutputStream outputStream = hdfs.create(path, true)) {
                            outputStream.writeUTF(record.value());
                        }

                        System.out.println("Сообщение записано в HDFS по пути: " + hdfsFilePath);

                    }
                } catch (Exception e) {
                     System.out.print(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
