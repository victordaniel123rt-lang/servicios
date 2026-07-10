package com.services.inventory_service.controller;

import com.services.inventory_service.model.dto.BaseResponse;
import com.services.inventory_service.model.dto.OrderItemsDTO;
import com.services.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class Controller {
    private final InventoryService service;

    @GetMapping("/{sku}")
    public ResponseEntity<Boolean> isInStock(@PathVariable("sku") String sku){
        return ResponseEntity.ok(service.inStock(sku));
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BaseResponse> areInStock(@RequestBody List<OrderItemsDTO> orderItems) {
        return Mono.fromCallable(() -> this.service.areInStock(orderItems))
                .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }



}
