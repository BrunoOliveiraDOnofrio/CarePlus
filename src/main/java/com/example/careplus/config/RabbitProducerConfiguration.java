package com.example.careplus.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProducerConfiguration {

    @Value("${app.rabbitmq.exchange.consultas-criadas}")
    private String consultasCriadasExchangeName;

    @Value("${app.rabbitmq.queue.consultas-criadas:consultas.criadas.queue}")
    private String queueName;

    @Value("${app.rabbitmq.routing-key.consultas-criadas}")
    private String routingKey;

    @Bean
    public DirectExchange consultasCriadasExchange() {
        return new DirectExchange(consultasCriadasExchangeName, true, false);
    }

    @Bean
    public Queue consultasCriadasQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding consultasCriadasBinding(Queue consultasCriadasQueue, DirectExchange consultasCriadasExchange) {
        return BindingBuilder
                .bind(consultasCriadasQueue)
                .to(consultasCriadasExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }
}

