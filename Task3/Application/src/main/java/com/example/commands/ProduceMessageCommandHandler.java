package com.example.commands;

import com.example.Message;
import com.example.ProhibitedWord;
import com.example.configuration.KafkaOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shortbus.RequestHandler;

@Component
public class ProduceMessageCommandHandler implements RequestHandler<ProduceMessageCommand, Boolean> {
    @Autowired
    KafkaOptions kafkaOptions;
    @Autowired
    KafkaProducer<String, String> getProducer;

    @Override
    public Boolean handle(ProduceMessageCommand request) {

        try {
            var producer = getProducer;

            var message = new Message();
            message.id = Uuid.randomUuid().toString();
            message.from = request.message.from;
            message.to = request.message.to;
            message.body = request.message.body;

            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());

            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // Отправка сообщения
            ProducerRecord<String, String> record = new ProducerRecord<>(kafkaOptions.stream.messagesTopicName, Uuid.randomUuid().toString(), jsonString);

            producer.send(record);

            System.out.printf("Отправлено сообщение: %s", jsonString);

            // Закрытие продюсера
            producer.close();

            return true;
        } catch (Exception e) {
            System.out.printf(e.toString());

            return false;
        }
    }
}