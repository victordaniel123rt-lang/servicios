package com.services.orders_service.service;

import com.services.orders_service.model.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> obtenerTodas();
    OrderDTO crearOrder(OrderDTO dto);
}
