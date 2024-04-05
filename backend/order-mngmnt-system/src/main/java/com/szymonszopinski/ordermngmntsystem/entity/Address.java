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
    @Pattern(regexp = "^[A-Za-ząĄćĆęĘłŁńŃóÓśŚżŻźŹ ]+$", message = "City can only contain letters")
    @Column(name="city")
    private String city;

    @NotBlank(message = "Country is mandatory")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Country can only contain letters")
    @Column(name="country")
    private String country;


    @NotBlank(message = "Province is mandatory")
    @Pattern(regexp = "^[A-Za-ząĄćĆęĘłŁńŃóÓśŚżŻźŹ ]+$", message = "City can only contain letters")
    @Column(name="province")
    private String province;

    @NotBlank(message = "Street is mandatory")
    @Pattern(regexp = "^[A-Za-ząĄćĆęĘłŁńŃóÓśŚżŻźŹ0-9]+(?:[\\s/][A-Za-ząĄćĆęĘłŁńŃóÓśŚżŻźŹ0-9]+)*$")
    @Column(name="street")
    private String street;

    @NotBlank
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Zip code must be in the format of 'XX-XXX', where 'X' are digits")
    @Column(name="zip_code")
    private String zipCode;
}

