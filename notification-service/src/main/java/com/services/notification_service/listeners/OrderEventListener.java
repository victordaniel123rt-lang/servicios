package com.services.notification_service.listeners;

import com.services.notification_service.events.OrderEvent;
import com.services.notification_service.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener{

    @KafkaListener(topics = "orders-topic")
    public void handleOrdersNotifications(String message){
    var orderEvent = JsonUtils.fromJson(message, OrderEvent.class);

    //send mail to customer, send SMS to customer, etc.
    //Notify another service

        log.info("order {} event received for order : {} with {} items", orderEvent.orderStatus(),orderEvent.orderNumber(),orderEvent.itemsCount());

    }
}
