package com.example.Task1.Controllers;

import com.example.Task1.Contracts.Good;
import com.example.Task1.Contracts.Order;
import com.example.Task1.Contracts.OrderMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/produce-message")
public class ProducerController {

    @GetMapping
    public void Produce() throws JsonProcessingException {

        // Конфигурация продюсера – адрес сервера, сериализаторы для ключа и значения.
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.31.11:19094,192.168.31.11:29094,192.168.31.11:39094");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer .class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer .class.getName());
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000); // Макс. время ожидания метаданных
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, "30000"); // Частота обновления метаданных (мс)

        // Важные настройки для At Least Once
        properties.put(ProducerConfig.ACKS_CONFIG, "all"); // Ждём подтверждения от всех реплик
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE); // Бесконечные попытки повтора
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // Запрещаем переупорядочивание
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // Идемпотентность

        // Создание продюсера
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        var contract = new OrderMessage();
        contract.DateTime = ZonedDateTime.now(ZoneOffset.UTC);
        contract.Id = UUID.randomUUID();

        var order = new Order();
        order.OrderId = UUID.randomUUID();
        order.CustomerEmail = "test@mail.ru";

        var good1 = new Good();
        good1.Name = "Good1";
        good1.Qty = 1;

        var good2 = new Good();
        good2.Name = "Good2";
        good2.Qty = 2;

        var good3 = new Good();
        good3.Name = "Good3";
        good3.Qty = 3;
        order.Goods = (new ArrayList<>() {{
            add(good1);
            add(good2);
            add(good3);
        }});

        contract.Order = order;

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        String jsonString = mapper.writeValueAsString(contract);

        // Отправка сообщения
        ProducerRecord<String, String> record = new ProducerRecord<>("orders-topic", contract.Id.toString(), jsonString);

        var result = producer.send(record);

        System.out.printf("Отправлено сообщение: %s", jsonString);

        // Закрытие продюсера
        producer.close();
    }

}
