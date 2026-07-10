package com.services.orders_service.service;

import com.services.orders_service.events.OrderEvent;
import com.services.orders_service.mapper.Mapper;
import com.services.orders_service.model.dto.BaseResponse;
import com.services.orders_service.model.dto.OrderDTO;
import com.services.orders_service.model.dto.OrderItemsDTO;
import com.services.orders_service.model.entities.Order;
import com.services.orders_service.model.entities.OrderItems;
import com.services.orders_service.model.enums.OrderStatus;
import com.services.orders_service.respository.OrderRepository;
import com.services.orders_service.utils.JsonUtils;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;
    private final ObservationRegistry observationRegistry;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public List<OrderDTO> obtenerTodas() {
        return repository.findAll().stream().map(Mapper::toOrderDTO).toList();
    }

    @Override
    public OrderDTO crearOrder(OrderDTO dto) {
        Observation inventoryObservation = Observation.createNotStarted("inventory-service",observationRegistry);
        return inventoryObservation.observe(()->{
            BaseResponse result = this.webClientBuilder.build()
                    .post()
                    .uri("lb://inventory-service/api/inventory/in-stock")
                    .bodyValue(dto.getOrderItems())
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();
            if(result!=null&& !result.hasErrors()){
                Order order = new Order();
                order.setOrderNumber(UUID.randomUUID().toString());
                order.setOrderItems(dto.getOrderItems().stream()
                        .map(orderItemsDTO -> mapOrderItemRequestToOrderItem(orderItemsDTO,order))
                        .toList());

                var savedOrder= this.repository.save(order);
                this.kafkaTemplate.send("orders-topic", JsonUtils.toJson(
                    new OrderEvent(savedOrder.getOrderNumber(), savedOrder.getOrderItems().size(), OrderStatus.PLACED)
                ));
                return Mapper.toOrderDTO(savedOrder);
            }else {
                    throw new IllegalArgumentException("Some of the products are not in stock");
            }
        });

    }


    private OrderItems mapOrderItemRequestToOrderItem(OrderItemsDTO orderItemRequest, Order order) {
        return OrderItems.builder()
                .id(orderItemRequest.getId())
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }


}
