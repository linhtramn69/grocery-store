package org.example.controller;

import org.example.model.TypeProduct;
import org.example.service.TypeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type-product")
public class TypeProductController {

    @Autowired
    private TypeProductService typeProductService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllType() {
        List<TypeProduct> listType = typeProductService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdType(@PathVariable("id") String id) {
        TypeProduct type = typeProductService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(type);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createType(@RequestBody TypeProduct type) {
        TypeProduct newType = typeProductService.create(type);
        return ResponseEntity.status(HttpStatus.OK).body(newType);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateType(@RequestBody TypeProduct type, @PathVariable("id") String id) {
        TypeProduct newType = typeProductService.update(type, id);
        return ResponseEntity.status(HttpStatus.OK).body(newType);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteType(@PathVariable String id) {
        typeProductService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
