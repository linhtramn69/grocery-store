package org.example.converter;

import org.example.model.Product;
import org.example.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductResponse toResponse(Product product){
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImage(product.getImage());
        response.setQuantity(product.getQuantity());
        response.setStatus(product.getStatus());
        product.getTypes().stream().forEach(item -> {
            response.getTypes().add(item);
        });
        return response;
    }
}
