package com.example.Task1.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@RestController
@RequestMapping("/api/produce-message")
public class ProducerController {

    @GetMapping
    public void Produce() {

        // Конфигурация продюсера – адрес сервера, сериализаторы для ключа и значения.
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.31.11:19094,192.168.31.11:29094,192.168.31.11:39094");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer .class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer .class.getName());

        // Создание продюсера
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Отправка сообщения
        ProducerRecord<String, String> record = new ProducerRecord<>("test-topic", "key-1", "message-1");

        producer.send(record, (_, ex) -> {
            if (ex != null) {
                System.err.println("Ошибка: " + ex.getMessage());
            }});

        // Закрытие продюсера
        producer.close();
    }

}
