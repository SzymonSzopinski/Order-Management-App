package com.szymonszopinski.ordermngmntsystem.repository;

import com.szymonszopinski.ordermngmntsystem.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
