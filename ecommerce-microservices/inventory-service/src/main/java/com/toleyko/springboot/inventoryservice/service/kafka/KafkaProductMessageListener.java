package com.toleyko.springboot.inventoryservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class KafkaProductMessageListener {
    private InventoryServiceImpl inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "all-products", groupId = "all-products-group-1")
    public void consume(String message) throws RemainderNotFoundException, JsonProcessingException {
        String[] msgArr = objectMapper.readValue(message, String[].class);
        switch (msgArr[0]) {
            case "save" -> {
                Remainder remainder = new Remainder()
                        .setName(msgArr[1])
                        .setLeft(0)
                        .setSold(0)
                        .setCost(new BigDecimal(msgArr[2]));
                inventoryService.saveRemainder(remainder);
                log.info(remainder + " - saved");
            }

            case "update" -> {
                String[] names = msgArr[1].split("~");
                Remainder oldRemainder = inventoryService.getRemainderByName(names[0]);
                inventoryService.updateRemainderById(oldRemainder.getId(), oldRemainder.setName(names[1]).setCost(new BigDecimal(msgArr[2])));
                log.info(oldRemainder + " - updated");
            }

            case "delete" -> {
                Long id = inventoryService.getRemainderByName(msgArr[1]).getId();
                inventoryService.deleteRemainderById(id);
                log.info("remainder with id = " + id + " - deleted");
            }
        }
    }
}
