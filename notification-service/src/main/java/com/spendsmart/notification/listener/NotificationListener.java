package com.spendsmart.notification.listener;

import com.rabbitmq.client.Channel;
import com.spendsmart.notification.config.RabbitMQConfig;
import com.spendsmart.notification.entity.Notification;
import com.spendsmart.notification.service.NotifService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotifService notifService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE, ackMode = "MANUAL")
    public void handleNotification(Map<String, Object> payload,
                                   Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag)
            throws IOException {
        try {
            log.info("Received message: {}", payload);

            Notification n = new Notification();
            n.setRecipientId(Long.parseLong(payload.get("recipientId").toString()));
            n.setType(payload.getOrDefault("type",     "INFO").toString());
            n.setSeverity(payload.getOrDefault("severity", "INFO").toString());
            n.setTitle(payload.getOrDefault("title",    "").toString());
            n.setMessage(payload.getOrDefault("message",  "").toString());

            notifService.send(n);

            // ✅ Acknowledge — message processed successfully
            channel.basicAck(tag, false);
            log.info("Notification saved and ACKed for user {}", n.getRecipientId());

        } catch (Exception e) {
            // ❌ NACK — send to Dead Letter Queue, do NOT requeue
            log.error("Failed to process notification, sending to DLQ: {}", e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }

    // ── Listen on DLQ (for monitoring / alerting) ─────────────
    @RabbitListener(queues = RabbitMQConfig.DLX_QUEUE)
    public void handleDeadLetter(Map<String, Object> payload) {
        log.error("DEAD LETTER received — manual intervention needed: {}", payload);
        // You can send an email alert here in the future
    }
}