package com.services.inventory_service.controller;

import com.services.inventory_service.model.dto.BaseResponse;
import com.services.inventory_service.model.dto.OrderItemsDTO;
import com.services.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus()
    public BaseResponse areInStock(@RequestBody List<OrderItemsDTO> orderItemsDTOS){
        return service.areInStock(orderItemsDTOS);
    }



}
