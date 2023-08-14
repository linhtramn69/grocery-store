package org.example.service;

import org.example.model.TypeProduct;

import java.util.List;

public interface TypeProductService {
    List<TypeProduct> findAll();

    TypeProduct create(TypeProduct type);

    TypeProduct findById(String id);

    TypeProduct update(TypeProduct type, String id);

    void delete(String id);
}
