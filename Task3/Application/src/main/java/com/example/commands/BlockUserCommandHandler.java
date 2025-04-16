package com.example.commands;

import com.example.BlockedUser;
import com.example.User;
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
public class BlockUserCommandHandler implements RequestHandler<BlockUserCommand, Boolean> {

    @Autowired
    KafkaOptions kafkaOptions;
    @Autowired
    KafkaProducer<String, String> getProducer;

    @Override
    public Boolean handle(BlockUserCommand request) {

        try {
            var producer = getProducer;

            var blockedUser = new BlockedUser();
            blockedUser.User = new User();
            blockedUser.User.Id = Uuid.randomUuid().toString();
            blockedUser.User.Name = request.userName;
            blockedUser.Blocked = new User();
            blockedUser.Blocked.Id = Uuid.randomUuid().toString();
            blockedUser.Blocked.Name = request.blockedUser;

            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule());

            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(blockedUser);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // Отправка сообщения
            ProducerRecord<String, String> record = new ProducerRecord<>(kafkaOptions.stream.blockedUsersTopicName, Uuid.randomUuid().toString(), jsonString);

            producer.send(record);

            System.out.printf("Отправлено сообщение: %s", jsonString);

            return true;
        } catch (Exception e) {
            System.out.printf(e.toString());

            return false;
        }
    }
}
