package com.spendsmart.budget.client;

import com.spendsmart.budget.config.RabbitMQConfig;
import com.spendsmart.budget.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final RabbitTemplate rabbitTemplate;

    public void sendBudgetAlert(Long recipientId, String title, String message,
                                String severity, Long relatedId) {
        NotificationEvent event = new NotificationEvent(
                recipientId, "BUDGET_ALERT", severity,
                title, message, relatedId, "BUDGET"
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "notification.budget.alert",   // routing key
                event
        );
    }
}