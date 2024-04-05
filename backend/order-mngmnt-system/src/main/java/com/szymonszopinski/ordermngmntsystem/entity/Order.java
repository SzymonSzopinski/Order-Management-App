package com.szymonszopinski.ordermngmntsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    @NotNull(message = "Branch is required")
    @Pattern(regexp = "Warszawa|Katowice|Lublin", message = "Invalid selection")
    @Column(name = "branch")
    private String branch;

    @NotNull(message = "Status is required")
    @Pattern(regexp = "Opłacone|Oczekiwanie na przelew", message = "Invalid selection")
    @Column(name="status")
    private String status;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must be in the past or present")
    @Column(name="date_created")
    private LocalDate dateCreated;

    @NotNull(message = "Delivery method is required")
    @Pattern(regexp = "UPS|DHL|InPost|Poczta Polska|Odbior w oddziale", message = "Invalid selection")
    @Column(name = "delivery_method")
    private String  deliveryMethod;

    @NotNull(message = "Payment method is required")
    @Pattern(regexp = "Platnosc przelewem|Platnosc gotowka|DHL", message = "Invalid selection")
    @Column(name = "payment_method")
    private String  paymentMethod;

    @Size(max = 200, message = "Note cannot be longer than 200 characters")
    @Column(name = "note_to_order")
    private String noteToOrder;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonManagedReference
    private Set<OrderedItems> orderItem = new HashSet<>();


    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than or equal to 0")
    @Digits(integer=8, fraction=2, message = "Total price can have up to 6 integer digits and 2 fractional digits")
    @NotNull(message = "Total price is required")
    @Column(name="total_price")
    private BigDecimal totalPrice;

    public void add(OrderedItems item) {
        if (item != null) {
            if (orderItem == null) {
                orderItem = new HashSet<>();
            }
            orderItem.add(item);
            item.setOrder(this);
        }
    }


}
