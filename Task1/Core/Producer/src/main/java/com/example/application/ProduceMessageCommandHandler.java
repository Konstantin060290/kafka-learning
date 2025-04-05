package com.example.application;

import com.example.contracts.Good;
import com.example.contracts.Order;
import com.example.contracts.OrderMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import factories.KafkaFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import shortbus.RequestHandler;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class ProduceMessageCommandHandler implements RequestHandler<ProduceMessageCommand, Boolean> {

    @Override
    public Boolean handle(ProduceMessageCommand request) {

        var factory = new KafkaFactory();
        var producer = factory.getProducer();

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

        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(contract);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Отправка сообщения
        ProducerRecord<String, String> record = new ProducerRecord<>("orders-topic", contract.Id.toString(), jsonString);

        producer.send(record);

        System.out.printf("Отправлено сообщение: %s", jsonString);

        // Закрытие продюсера
        producer.close();

        return true;
    }
}