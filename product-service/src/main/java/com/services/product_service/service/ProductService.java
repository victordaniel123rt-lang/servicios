package com.services.product_service.service;


import com.services.product_service.model.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> buscarTodos();
    ProductDTO crear(ProductDTO dto);
}
