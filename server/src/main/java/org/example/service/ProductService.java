package org.example.service;

import org.example.model.OrderProduct;
import org.example.model.Orders;
import org.example.model.Product;
import org.example.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductService {
    Set<ProductResponse> findAll();

    Product create(Product product, MultipartFile image) throws IOException;

    Product findById(Long id);

    Product update(Product product, MultipartFile image, Long id) throws IOException;

    void delete(Long id);

    Set<Product> findByIdType(String id);

    Set<ProductResponse> searchByKeyword(String keyword);

    List<ProductResponse> findPopularProductsByYear(int year);
}
