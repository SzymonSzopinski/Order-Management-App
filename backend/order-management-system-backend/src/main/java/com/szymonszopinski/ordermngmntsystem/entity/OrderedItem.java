package com.szymonszopinski.ordermngmntsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@Table(name="order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name="quantity")
    private int quantity;

    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @Column(name="product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull(message = "Each ordered item must be associated with an order")
    @JsonBackReference
    private Order order;


}
