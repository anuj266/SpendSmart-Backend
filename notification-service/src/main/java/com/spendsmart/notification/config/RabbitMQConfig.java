package com.spendsmart.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ── Main exchange & queue ──────────────────────────────────
    public static final String EXCHANGE    = "spendsmart.exchange";
    public static final String QUEUE       = "notification.queue";
    public static final String ROUTING_KEY = "notification.#";

    // ── Dead Letter exchange & queue ──────────────────────────
    public static final String DLX_EXCHANGE    = "spendsmart.dlx";
    public static final String DLX_QUEUE       = "notification.dead.queue";
    public static final String DLX_ROUTING_KEY = "notification.dead";

    // ── Main exchange ─────────────────────────────────────────
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    // ── Main queue (points to DLX on failure) ─────────────────
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange",    DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                .build();
    }

    // ── Bind main queue to main exchange ──────────────────────
    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    // ── Dead letter exchange ──────────────────────────────────
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    // ── Dead letter queue ─────────────────────────────────────
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    // ── Bind DLQ to DLX ──────────────────────────────────────
    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue,
                                     DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DLX_ROUTING_KEY);
    }

    // ── JSON converter ────────────────────────────────────────
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}