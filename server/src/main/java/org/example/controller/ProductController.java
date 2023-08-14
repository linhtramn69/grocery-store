package org.example.controller;

import org.example.model.OrderProduct;
import org.example.model.Orders;
import org.example.model.Product;
import org.example.response.ProductResponse;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProduct() {
        Set<ProductResponse> listProduct = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listProduct);
    }

    @GetMapping("/getPopularProductsByYear/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPopularProductsByYear(@PathVariable("year") int year){
        List<ProductResponse> listProduct = productService.findPopularProductsByYear(year);
        return ResponseEntity.status(HttpStatus.OK).body(listProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdProduct(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/getByType/{id}")
    public ResponseEntity<?> findByIdType(@PathVariable("id") String id) {
        Set<Product> product = productService.findByIdType(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @RequestMapping(value = {"/search/", "/search/{keyword}"}, method = RequestMethod.GET)
    public ResponseEntity<?> searchByKeyword(@PathVariable("keyword") Optional<String> keyword) {
        Set<ProductResponse> product = new HashSet<>();
        if (keyword.isPresent()) {
            product = productService.searchByKeyword(keyword.get());
        } else {
            product = productService.findAll();
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestPart(name = "image", required = false) MultipartFile image,
                                           @RequestPart(name = "product") Product product) throws IOException {
        Product newProduct = productService.create(product, image);
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@RequestPart(name = "image", required = false) MultipartFile image,
                                           @RequestPart Product product, @PathVariable("id") Long id) throws IOException {
        Product newProduct = productService.update(product, image, id);
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
