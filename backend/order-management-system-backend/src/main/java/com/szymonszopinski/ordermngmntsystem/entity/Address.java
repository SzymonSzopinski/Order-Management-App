package com.szymonszopinski.ordermngmntsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name="address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "City is mandatory")
    @Column(name="city")
    private String city;

    @NotBlank(message = "Country is mandatory")
    @Column(name="country")
    private String country;


    @NotBlank(message = "Province is mandatory")
    @Column(name="province")
    private String province;

    @NotBlank(message = "Street is mandatory")
    @Column(name="street")
    private String street;

    @NotBlank( message = "House number is mandatory")
    @Column(name="house_number")
    private String houseNumber;

    @NotBlank
    @Pattern(regexp = "\\d{2}-\\d{3}|\\d{5}(-\\d{4})?", message = "Zip code must be in the format of 'XX-XXX' or 'XXXXX' or 'XXXXX-XXXX', where 'X' are digits")
    @Column(name="zip_code")
    private String zipCode;
}

