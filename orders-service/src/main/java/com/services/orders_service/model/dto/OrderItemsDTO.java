package com.services.orders_service.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemsDTO {
    private Long id;
    private String sku;
    private Double price;
    private Long quantity;
    private Long order;
}
