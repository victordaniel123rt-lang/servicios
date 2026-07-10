package com.services.orders_service.controller;

import com.services.orders_service.model.dto.OrderDTO;
import com.services.orders_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {



    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "order-service", fallbackMethod = "placeOrderFallback")
    public Mono<ResponseEntity<OrderDTO>> placeOrder(@RequestBody OrderDTO dto){
        return Mono.fromCallable(() -> this.service.crearOrder(dto))
                .map(orders -> ResponseEntity.status(HttpStatus.CREATED).body(orders))
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> obtenerOrdenes(){ return this.service.obtenerTodas();}

    private ResponseEntity<OrderDTO> placeOrderFallback(OrderDTO dto, Throwable throwable){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }


}
