package com.szymonszopinski.ordermngmntsystem.service;

import com.szymonszopinski.ordermngmntsystem.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    Product addProduct(Product product);

    List<Product> getProducts();

    Product updateProduct(Product product, Long id);

   Product getProductById(Long id);

    void deleteProduct(Long id);

}
