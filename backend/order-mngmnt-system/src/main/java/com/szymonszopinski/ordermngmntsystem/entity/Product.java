package com.szymonszopinski.ordermngmntsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Entity
@Table(name="product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 6, max = 50, message = "Product name must be between 6 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Product name can only contain letters, numbers, and spaces")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(min = 6, max = 250, message = "Product name must be between 6 and 250 characters")
    @Pattern(regexp = "^[A-Za-z0-9ąĄćĆęĘłŁńŃóÓśŚżŻźŹ .,]+$", message = "Description can only contain letters, numbers, spaces, dots, and commas")    @Column(name = "description")
    private String description;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than or equal to 0")
    @Digits(integer = 6, fraction = 2, message = "Unit price must not exceed 6 digits in the integer part and 2 digits in the fractional part")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "image_url")
    @URL(message = "Invalid URL format")
    private String imageUrl;


}
