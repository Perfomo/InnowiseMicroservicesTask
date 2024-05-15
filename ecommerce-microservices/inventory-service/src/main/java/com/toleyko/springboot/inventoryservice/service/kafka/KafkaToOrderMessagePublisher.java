package com.toleyko.springboot.inventoryservice.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaToOrderMessagePublisher {
    private KafkaTemplate<String, Object> template;
    public void sendMessageToTopic(String message) {
        System.out.println("Pub send order");
        CompletableFuture<SendResult<String, Object>> future = template.send("handled-orders", message);
        future.whenComplete((result, exc) -> {
            if (exc == null) {
                System.out.println("Sent msg=[" + message +
                        "] with offset = [" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message = [" +
                        message + "] due to: " + exc.getMessage());
            }
        });
    }

    @Autowired
    public void setTemplate(KafkaTemplate<String, Object> template) {
        this.template = template;
    }
}
