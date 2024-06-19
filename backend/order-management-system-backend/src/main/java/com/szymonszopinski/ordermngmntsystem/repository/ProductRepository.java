package com.szymonszopinski.ordermngmntsystem.repository;

import com.szymonszopinski.ordermngmntsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
