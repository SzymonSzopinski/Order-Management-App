package com.szymonszopinski.ordermngmntsystem.entity;

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
    @Size(max = 15)
    @Column(name = "first_name")
    @Pattern(regexp = "^[A-Za-ząĄćĆęĘłŁńŃóÓśŚżŻźŹ]*$")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^[A-Za-ząĄćĆęĘłŁńŃóÓśŚżŻźŹ]*$")
    @Size(min = 2, max = 15)
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "The email address format is incorrect")
    @Size(min = 5, max = 50)
    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private Address address;

    @Pattern(regexp = "\\d{9}")
    @Column(name = "phone_number")
    private String phoneNumber;



}
