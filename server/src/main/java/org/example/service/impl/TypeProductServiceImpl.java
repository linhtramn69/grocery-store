package org.example.service.impl;

import org.example.exception.ApiRequestException;
import org.example.model.TypeProduct;
import org.example.repository.TypeProductRepository;
import org.example.service.TypeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.example.constants.ErrorMessage.TYPE_PRODUCT_IN_USE;
import static org.example.constants.ErrorMessage.TYPE_PRODUCT_NOT_FOUND;

@Service
public class TypeProductServiceImpl implements TypeProductService {
    @Autowired
    TypeProductRepository typeProductRepository;

    @Override
    public List<TypeProduct> findAll() {
        return typeProductRepository.findAll();
    }

    @Override
    public TypeProduct findById(String id) {
        return typeProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(TYPE_PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public TypeProduct create(TypeProduct type) {
        Optional<TypeProduct> typeDB = typeProductRepository.findById(type.getId());
        if (typeDB.isPresent()) {
            throw new ApiRequestException(TYPE_PRODUCT_IN_USE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return typeProductRepository.save(type);
    }

    @Override
    public TypeProduct update(TypeProduct type, String id) {
        TypeProduct typeDB = typeProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(TYPE_PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        typeDB.setName(type.getName());
        return typeProductRepository.save(typeDB);
    }

    @Override
    public void delete(String id) {
        TypeProduct typeDB = typeProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(TYPE_PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        typeProductRepository.deleteById(id);
    }


}
