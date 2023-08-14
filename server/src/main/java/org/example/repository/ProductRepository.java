package org.example.repository;

import org.example.model.OrderProduct;
import org.example.model.Orders;
import org.example.model.Product;
import org.example.model.TypeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    Set<Product> findByTypes(TypeProduct type);

    @Query("SELECT p FROM Product p WHERE CONCAT(p.name) LIKE %?1%")
    Set<Product> findByKeyword(String keyword);

    @Query("select o from Orders o where year(o.createdDate) = ?1")
    List<Orders> findOrdersByYear(int year);

    @Query("select op from OrderProduct op where op.order in ?1")
    List<OrderProduct> findOrderProductByOrder(List<Orders> listOrders);

}
