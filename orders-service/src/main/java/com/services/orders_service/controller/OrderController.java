package com.services.orders_service.controller;

import com.services.orders_service.model.dto.OrderDTO;
import com.services.orders_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {



    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "order-service", fallbackMethod = "placeOrderFallback")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO dto){
    var orders = this.service.crearOrder(dto);
    return ResponseEntity.ok(orders);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> obtenerOrdenes(){ return this.service.obtenerTodas();}

    private ResponseEntity<OrderDTO> placeOrderFallback(OrderDTO dto, Throwable throwable){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }


}
