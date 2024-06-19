package com.szymonszopinski.ordermngmntsystem.repository;

import com.szymonszopinski.ordermngmntsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
