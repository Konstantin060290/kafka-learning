package com.example;


import com.example.configuration.KafkaOptions;
import com.example.factories.KafkaFactory;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProducerTopic2 {

    @Autowired
    private KafkaOptions _kafkaOptions;
    @Autowired
    private KafkaFactory _kafkaFactory;

    public ProducerTopic2() {
    }

    @PostConstruct
    public void init() {
        (new Thread(this::ProduceSomething)).start();
    }

    public void ProduceSomething() {

        try {
            var producer = _kafkaFactory.getProducer();

            var message = "Тестовое сообщение в топик 2";

            // Отправка сообщения
            ProducerRecord<String, String> record = new ProducerRecord<>(_kafkaOptions.consumer.topic2, Uuid.randomUuid().toString(), message);

            producer.send(record);

            System.out.printf("Отправлено сообщение: %s", message);

        } catch (
                Exception e) {
            System.out.printf(e.toString());
        }
    }

}
