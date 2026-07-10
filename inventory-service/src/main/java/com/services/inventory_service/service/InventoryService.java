package com.services.inventory_service.service;

import com.services.inventory_service.model.dto.BaseResponse;
import com.services.inventory_service.model.dto.OrderItemsDTO;

import java.util.List;

public interface InventoryService {
    boolean inStock(String sku);
    BaseResponse areInStock(List<OrderItemsDTO> orderItemsDTOS);
}
