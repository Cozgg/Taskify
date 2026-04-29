package com.ccq.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.ccq.configs.RabbitMQConfig;
import com.ccq.pojo.message.NotificationMessage;
import com.rabbitmq.client.Channel;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationConsumer.class);
    private static final Set<String> PROCESSED_EVENT_IDS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE, ackMode = "MANUAL")
    public void consume(NotificationMessage message, Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        if (message == null || message.getRecipientEmails() == null || message.getRecipientEmails().isEmpty()) {
            log.info("Skip empty email notification");
            channel.basicAck(tag, false);
            return;
        }

        if (message.getEventId() != null && PROCESSED_EVENT_IDS.contains(message.getEventId())) {
            log.info("Skip duplicate email notification eventId={} type={} cardId={} workspaceId={}",
                    message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId());
            channel.basicAck(tag, false);
            return;
        }

        List<String> validEmails = message.getRecipientEmails().stream()
                .filter(this::isValidEmail)
                .distinct()
                .collect(Collectors.toList());

        if (validEmails.isEmpty()) {
            log.info("Skip notification eventId={} type={} cardId={} workspaceId={} because no valid recipient email",
                    message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId());
            channel.basicAck(tag, false);
            return;
        }

        try {
            sendWithRetry(validEmails, message);
            if (message.getEventId() != null && !message.getEventId().isBlank()) {
                PROCESSED_EVENT_IDS.add(message.getEventId());
            }
            channel.basicAck(tag, false);
        } catch (Exception ex) {
            log.error("Fail send mail eventId={} type={} cardId={} workspaceId={} to={}",
                    message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId(),
                    validEmails, ex);
            channel.basicNack(tag, false, false);
        }
    }

    private void sendWithRetry(List<String> emails, NotificationMessage message) throws MessagingException {
        MessagingException lastException = null;
        for (int i = 0; i < 3; i++) {
            try {
                send(emails, message);
                log.info("Send mail eventId={} type={} cardId={} workspaceId={} to={}",
                        message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId(),
                        emails);
                return;
            } catch (MessagingException ex) {
                lastException = ex;
                log.error("Fail send mail eventId={} type={} cardId={} workspaceId={} to={} attempt={}",
                        message.getEventId(), message.getType(), message.getCardId(), message.getWorkspaceId(),
                        emails, i + 1, ex);
                if (i == 2) {
                    throw ex;
                }
            }
        }
        throw lastException;
    }

    private void send(List<String> bccEmails, NotificationMessage message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        String senderEmail = getMailUsername();
        String toEmail = getMailToAddress();
        if (!isValidEmail(senderEmail)) {
            throw new MessagingException("mail.username is missing or invalid");
        }
        if (!isValidEmail(toEmail)) {
            throw new MessagingException("mail.to-address is missing or invalid");
        }
        helper.setTo(toEmail);
        helper.setBcc(bccEmails.toArray(new String[0]));
        helper.setSubject(message.getSubject());
        helper.setText(message.getBodyWithAuditInfo(), false);

        if (senderEmail != null && !senderEmail.isBlank()) {
            helper.setFrom(senderEmail);
        }

        mailSender.send(mimeMessage);
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.isBlank() && email.contains("@");
    }

    private String getMailToAddress() {
        String value = System.getenv("MAIL_TO_ADDRESS");
        if (value != null && !value.isBlank()) {
            return value;
        }
        return env.getProperty("mail.to-address");
    }

    private String getMailUsername() {
        String value = System.getenv("MAIL_USERNAME");
        if (value != null && !value.isBlank()) {
            return value;
        }
        return env.getProperty("mail.username");
    }
}
