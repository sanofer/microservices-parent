package com.javaprojects.productservice.service;

import com.javaprojects.productservice.dto.ProductRequest;
import com.javaprojects.productservice.dto.ProductResponse;
import com.javaprojects.productservice.model.Product;
import com.javaprojects.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
    }

    public List<ProductResponse> getProducts(){
       List<Product> products =  productRepository.findAll();
       List<ProductResponse> productResponses = products.stream()
               .map(product -> new ProductResponse().builder()
                       .id(product.getId())
                       .name(product.getName())
                       .description(product.getDescription())
                       .price(product.getPrice())
                       .build())
               .collect(Collectors.toList());
       return productResponses;
    }
}
