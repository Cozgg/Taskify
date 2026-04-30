package com.ccq.service;

import com.ccq.configs.RabbitMQConfig;
import com.ccq.pojo.message.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificationProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailNotification(NotificationMessage message) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.EMAIL_ROUTING_KEY,
                    message
            );
            log.info("Published email notification eventId={} type={} cardId={} workspaceId={} recipients={}",
                    message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId(),
                    message.getRecipientEmails());
        } catch (AmqpException ex) {
            log.error("Publish email notification failed eventId={} type={} cardId={} workspaceId={} recipients={}",
                    message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId(),
                    message.getRecipientEmails(), ex);
        }
    }
}
