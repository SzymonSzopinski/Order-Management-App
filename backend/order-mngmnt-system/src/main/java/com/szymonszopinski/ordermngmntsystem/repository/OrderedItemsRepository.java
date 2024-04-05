package com.szymonszopinski.ordermngmntsystem.repository;

import com.szymonszopinski.ordermngmntsystem.entity.OrderedItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface OrderedItemsRepository extends JpaRepository<OrderedItems, Long > {

    void deleteAllByIdIn(Set<Long> ids);

}
