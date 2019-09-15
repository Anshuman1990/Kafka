package com.example.Kafka.service;

import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.*;

@Service
public class Consumer {

    private final Logger logger = getLogger(Consumer.class);
    @KafkaListener(topics = "users", groupId = "group_id")

    public void consume(String message){
        logger.info(String.format("$$ -> Consumed Message -> %s",message));
    }
}
