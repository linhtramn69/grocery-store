package org.example.repository;

import org.example.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("select sum(o.total) from Orders o where o.status=2 and month(o.createdDate) = ?1 and year(o.createdDate) = ?2")
    Double getTotalByMonthAndYear(int i, int year);
}
