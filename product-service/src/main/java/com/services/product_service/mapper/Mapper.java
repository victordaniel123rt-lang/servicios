package com.services.product_service.mapper;

import com.services.product_service.model.dto.ProductDTO;
import com.services.product_service.model.entity.Product;


public class Mapper {



    public static ProductDTO toProductDTO(Product entity){
        if(entity == null) return null;
        return   ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }

    public static Product toProduct(ProductDTO dto){
        if(dto==null) return null;

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .sku(dto.getSku())
                .status(dto.getStatus())
                .price(dto.getPrice())
                .build();
    }


}
