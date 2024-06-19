package com.szymonszopinski.ordermngmntsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "The email address format is incorrect")
    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private Address address;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}|\\d{9}", message = "Please enter a valid phone number. For US: e.g., (123) 456-7890, for others: e.g., 123456789")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "customer_note_to_order")
    private String customerNoteToOrder;

    @JsonBackReference
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Order order;


}
