package com.toleyko.springboot.userservice.service.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaToOrderMessagePublisher {
    private KafkaTemplate<String, Object> template;
    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future = template.send("deleted-users-orders", message);
        future.whenComplete((result, exc) -> {
            Optional.ofNullable(exc).ifPresentOrElse(
                    e -> log.info("Unable to send message = [" + message + "] due to: " + e.getMessage()),
                    () -> log.info("Sent msg=[" + message + "] with offset = [" + result.getRecordMetadata().offset() + "]")
            );
        });
    }
}
