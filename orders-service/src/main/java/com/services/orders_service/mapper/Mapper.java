package com.services.orders_service.mapper;

import com.services.orders_service.model.dto.OrderDTO;
import com.services.orders_service.model.dto.OrderItemsDTO;
import com.services.orders_service.model.entities.Order;
import com.services.orders_service.model.entities.OrderItems;

import java.util.List;


public class Mapper {

    public static OrderDTO toOrderDTO(Order entity){
        if(entity==null) return null;
        List<OrderItemsDTO> lista = entity.getOrderItems().stream().map(Mapper::toOrderItemsDTO).toList();
        return OrderDTO.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .orderItems(lista)
                .build();
    }

    public static OrderItemsDTO toOrderItemsDTO(OrderItems dto){
        if(dto ==null) return null;
        return OrderItemsDTO.builder()
                .id(dto.getId())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }


    public static Order toOrder(OrderDTO dto){
        if(dto==null) return null;
        List<OrderItems> lista = dto.getOrderItems().stream().map(Mapper::toOrderItems).toList();
        return  Order.builder()
                .id(dto.getId())
                .orderNumber(dto.getOrderNumber())
                .orderItems(lista)
                .build();
    }

    public static OrderItems toOrderItems(OrderItemsDTO dto){
        if (dto==null) return  null;
        return OrderItems.builder()
                .id(dto.getId())
                .order(Order.builder().id(dto.getOrder()).build())
                .price(dto.getPrice())
                .sku(dto.getSku())
                .quantity(dto.getQuantity())
                .build();


    }






}
