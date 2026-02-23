package com.cs6650.shoppingcartservice;

import java.beans.BeanProperty;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RmqConfig {

  @Bean
  public Queue shipQueue(@Value("${ship.queue:ship-order}") String name) {
    return new Queue(name, true);
  }

  // Update: Convert message to JSON for RabbitMQ
  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}