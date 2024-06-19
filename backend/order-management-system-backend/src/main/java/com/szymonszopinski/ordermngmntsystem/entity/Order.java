package com.szymonszopinski.ordermngmntsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "branch")
    private String branch;

    @Column(name="status")
    private String status;

    @Column(name="date_created")
    private LocalDate dateCreated;

    @Column(name = "delivery_method")
    private String  deliveryMethod;

    @Column(name = "payment_method")
    private String  paymentMethod;

    @Column(name = "note_to_order")
    private String noteToOrder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonManagedReference
    private Set<OrderedItem> orderItem = new HashSet<>();

    @Valid
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @Column(name="total_price")
    private BigDecimal totalPrice;

    public void add(OrderedItem item) {
        if (item != null) {
            if (orderItem == null) {
                orderItem = new HashSet<>();
            }
            orderItem.add(item);
            item.setOrder(this);
        }
    }


}
