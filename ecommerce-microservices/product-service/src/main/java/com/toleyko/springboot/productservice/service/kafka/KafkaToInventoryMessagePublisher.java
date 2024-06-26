package com.toleyko.springboot.productservice.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaToInventoryMessagePublisher {
    private KafkaTemplate<String, Object> template;
    public void sendMessageToTopic(String message) {
        CompletableFuture<SendResult<String, Object>> future = template.send("all-products", message);
        future.whenComplete((result, exc) -> {
            Optional.ofNullable(exc).ifPresentOrElse(
                    e -> log.info("Unable to send message = [" + message + "] due to: " + e.getMessage()),
                    () -> log.info("Sent msg=[" + message + "] with offset = [" + result.getRecordMetadata().offset() + "]")
            );
        });
    }
    @Autowired
    public void setTemplate(KafkaTemplate<String, Object> template) {
        this.template = template;
    }
}
