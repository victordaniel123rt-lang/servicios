package com.services.product_service.service;

import com.services.product_service.mapper.Mapper;
import com.services.product_service.model.dto.ProductDTO;
import com.services.product_service.model.entity.Product;
import com.services.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository repository;

    @Override
    public List<ProductDTO> buscarTodos() {
        return repository.findAll().stream().map(Mapper::toProductDTO).toList();
    }

    @Override
    public ProductDTO crear(ProductDTO dto) {
        Product producto = Mapper.toProduct(dto);
        Product guardado = repository.save(producto);
        log.info("Producto añadido con exito: {}", producto);
        return Mapper.toProductDTO(guardado);
    }
}
