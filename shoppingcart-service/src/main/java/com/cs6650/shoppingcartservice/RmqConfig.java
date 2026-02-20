package com.cs6650.shoppingcartservice;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RmqConfig {

  @Bean
  public Queue shipQueue(@Value("${ship.queue:ship-order}") String name) {
    return new Queue(name, true);
  }
}