package org.example.repository;

import org.example.model.TypeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeProductRepository extends JpaRepository<TypeProduct, String> {
}
