package com.szymonszopinski.ordermngmntsystem.repository;

import com.szymonszopinski.ordermngmntsystem.entity.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderedItemsRepository extends JpaRepository<OrderedItem, Long > {

    void deleteAllByIdIn(Set<Long> ids);

}
