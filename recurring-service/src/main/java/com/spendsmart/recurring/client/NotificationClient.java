package com.spendsmart.recurring.client;

import com.spendsmart.recurring.config.RabbitMQConfig;
import com.spendsmart.recurring.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final RabbitTemplate rabbitTemplate;

    public void sendReminder(Long userId, String title, String message, Long relatedId) {
        NotificationEvent event = new NotificationEvent(
                userId, "RECURRING_REMINDER", "INFO",
                title, message, relatedId, "RECURRING"
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "notification.recurring.reminder",
                event
        );
    }
}