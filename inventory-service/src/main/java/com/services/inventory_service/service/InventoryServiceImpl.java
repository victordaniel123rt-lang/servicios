package com.services.inventory_service.service;

import com.services.inventory_service.model.dto.BaseResponse;
import com.services.inventory_service.model.dto.OrderItemsDTO;
import com.services.inventory_service.model.entities.Inventory;
import com.services.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository repository;

    @Override
    public boolean inStock(String sku) {
        var inventory = repository.findBySku(sku);
        return inventory.filter(value -> value.getQuantity()>0).isPresent();
    }

    @Override
    public BaseResponse areInStock(List<OrderItemsDTO> orderItemsDTOS) {
        var errors = new ArrayList<>();
        List<String> skus = orderItemsDTOS.stream().map(OrderItemsDTO::getSku).toList();
        List<Inventory> inventoryList = repository.findBySkuIn(skus);
        orderItemsDTOS.forEach(orderItem -> {
            var inventory = inventoryList.stream().filter(value -> value.getSku().equals(orderItem.getSku())).findFirst();
            if (inventory.isEmpty()) {
                errors.add("Product with sku" + orderItem.getSku() + " does not exists");
            } else if (inventory.get().getQuantity()<orderItem.getQuantity()){
                errors.add("Product with sku" + orderItem.getSku() + " has insufficient quantity");
            }});

        return errors.size()>0 ? new BaseResponse(errors.toArray(new String[0])) : new BaseResponse(null);
    }
}
