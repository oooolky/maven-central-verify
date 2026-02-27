package com.cs6650.warehouseconsumer;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class WarehouseListener {

  private final LongAdder totalOrders = new LongAdder();
  private final ConcurrentHashMap<Long, LongAdder> qtyByProduct = new ConcurrentHashMap<>();

  @Bean
  public Queue shipQueue(@Value("${ship.queue}") String queueName) {
    return new Queue(queueName, true);
  }

  // ✅ Debug version: receive String so it always works
  @RabbitListener(queues = "${ship.queue}", concurrency = "${consumer.concurrency:8}")
  public void onMessage(ShipMessage msg, Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
    try {
      totalOrders.increment();
      for (ShipMessage.Item item : msg.items()) {
        qtyByProduct.computeIfAbsent(item.productId(), k -> new LongAdder())
            .add(item.quantity());
      }
      channel.basicAck(tag, false);
    } catch (Exception e) {
      channel.basicNack(tag, false, true);
    }
  }

  @jakarta.annotation.PreDestroy
  public void onShutdown() {
    System.out.println("=== Warehouse shutting down ===");
    System.out.println("Total orders: " + totalOrders.sum());
    qtyByProduct.forEach((productId, quantity) -> {
      System.out.println("ProductID: " + productId + " | Total Quantity: " + quantity.sum());
    });
  }
}
