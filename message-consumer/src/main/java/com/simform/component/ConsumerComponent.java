package com.simform.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ConsumerComponent {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerComponent.class);

    @Autowired
    private AmqpTemplate template;

    @RabbitListener(queues = "SimformQueue")
    public void receiveMessage(@Payload String message) {
        logger.info("Received message is {}", message);
    }
}
