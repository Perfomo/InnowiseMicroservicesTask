//package com.toleyko.springboot.orderservice.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.toleyko.springboot.inventoryservice.entity.Remainder;
//import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaMessageListener {
//    private InventoryServiceImpl inventoryService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @KafkaListener(topics = "all-products", groupId = "all-products-group-1")
//    public void consume(String message) throws RemainderNotFoundException, JsonProcessingException {
//        String[] msgArr = objectMapper.readValue(message, String[].class);
//        switch (msgArr[0]) {
//            case "save":
//                Remainder remainder = new Remainder().setName(msgArr[1]).setLeft(0).setSold(0);
//                inventoryService.saveRemainder(remainder);
//                System.out.println(remainder + " - saved");
//                break;
//
//            case "update":
//                String[] names = msgArr[1].split("~");
//                Remainder oldRemainder = inventoryService.getRemainderByName(names[0]);
//                inventoryService.updateRemainderById(oldRemainder.getId(), oldRemainder.setName(names[1]));
//                System.out.println(oldRemainder + " - updated");
//                break;
//
//            case "delete":
//                Integer id = inventoryService.getRemainderByName(msgArr[1]).getId();
//                inventoryService.deleteRemainderById(id);
//                System.out.println("remainder with id = " + id + " - deleted");
//                break;
//        }
//    }
//
//    @Autowired
//    public void setInventoryService(InventoryServiceImpl inventoryService) {
//        this.inventoryService = inventoryService;
//    }
//}
