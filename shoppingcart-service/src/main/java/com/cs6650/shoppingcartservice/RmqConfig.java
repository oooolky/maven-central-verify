package com.cs6650.shoppingcartservice;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RmqConfig {

  @Bean
  public Queue shipQueue(@Value("${ship.queue}") String queueName) {
    // durable queue
    return new Queue(queueName, true);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(RabbitTemplate template) {
    // 让 returns 生效（便于排错）
    template.setMandatory(true);
    return template;
  }
}
