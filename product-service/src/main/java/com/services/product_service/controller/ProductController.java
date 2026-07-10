package com.services.product_service.controller;

import com.services.product_service.model.dto.ProductDTO;
import com.services.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductDTO> crear(@RequestBody ProductDTO dto){
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listarTodos(){
        return ResponseEntity.ok(service.buscarTodos());
    }


}
