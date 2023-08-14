package org.example.service.impl;

import org.example.converter.ProductConverter;
import org.example.exception.ApiRequestException;
import org.example.model.OrderProduct;
import org.example.model.Orders;
import org.example.model.Product;
import org.example.model.TypeProduct;
import org.example.repository.OrdersRepository;
import org.example.repository.ProductRepository;
import org.example.repository.TypeProductRepository;
import org.example.response.ProductResponse;
import org.example.service.FileStorageService;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.example.constants.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.example.constants.ErrorMessage.TYPE_PRODUCT_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductConverter converter;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    TypeProductRepository typeProductRepository;
    private Integer sum = 0;

    @Override
    public Set<ProductResponse> findAll() {
        List<Product> list = productRepository.findAll();
        Set<ProductResponse> listReturn = new HashSet<>();
        list.forEach(item -> {
            listReturn.add(converter.toResponse(item));
        });
        return listReturn;
    }

    @Override
    public List<ProductResponse> findPopularProductsByYear(int year) {
        List<Orders> listO = productRepository.findOrdersByYear(year);
        List<OrderProduct> listOP = productRepository.findOrderProductByOrder(listO);

        List<Product> listP = new ArrayList<>();
        listOP.forEach(item -> {
            if (!listP.contains(item.getProduct())) {
                item.setQuantity(item.getQuantity());
                listP.add(item.getProduct());
            }
        });

        List<ProductResponse> listResponse = new ArrayList<>();
        listP.forEach(item -> {
            listOP.forEach(op -> {
                if (op.getProduct().equals(item)) {
                    sum = sum + op.getQuantity();
                }
            });
            item.setQuantity(sum);
            sum = 0;
            listResponse.add(converter.toResponse(item));
        });

        return listResponse;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Set<Product> findByIdType(String id) {
        TypeProduct typeDB = typeProductRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(TYPE_PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        return productRepository.findByTypes(typeDB);
    }

    @Override
    public Set<ProductResponse> searchByKeyword(String keyword) {
        Set<Product> list = productRepository.findByKeyword(keyword);
        Set<ProductResponse> listReturn = new HashSet<>();
        list.forEach(item -> {
            listReturn.add(converter.toResponse(item));
        });
        return listReturn;
    }

    @Override
    public Product create(Product product, MultipartFile image) throws IOException {
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String uploadDir = "api/uploads/products";
            FileStorageService.saveFile(uploadDir, fileName, image);
            product.setImage(fileName);
            System.out.println(uploadDir + fileName + image);
        }
        product.getTypes().forEach(item -> {
            TypeProduct typeDB = typeProductRepository.findById(item.getId())
                    .orElseThrow(() -> {
                        product.getTypes().remove(item);
                        return new ApiRequestException(TYPE_PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
                    });
        });
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product, MultipartFile image, Long id) throws IOException {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setPrice(product.getPrice());
        productDB.setQuantity(product.getQuantity());
        productDB.setStatus(product.getStatus());
        if (!image.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String uploadDir = "uploads/products";
            FileStorageService.saveFile(uploadDir, fileName, image);
            productDB.setImage(fileName);
        }
        productDB.getTypes().clear();
        product.getTypes().forEach(item -> {
            TypeProduct typeDB = typeProductRepository.findById(item.getId())
                    .orElseThrow(() -> {
                        product.getTypes().remove(item);
                        return new ApiRequestException(TYPE_PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
                    });
            productDB.getTypes().add(typeDB);
        });
        return productRepository.save(productDB);
    }

    @Override
    public void delete(Long id) {
        Product productDB = productRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
        productRepository.deleteById(id);
    }

}
