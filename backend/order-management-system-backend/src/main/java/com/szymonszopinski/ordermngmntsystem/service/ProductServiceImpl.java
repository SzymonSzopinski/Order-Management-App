package com.szymonszopinski.ordermngmntsystem.service;

import com.szymonszopinski.ordermngmntsystem.entity.Product;
import com.szymonszopinski.ordermngmntsystem.exception.ProductNotFoundException;
import com.szymonszopinski.ordermngmntsystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product updateProduct(Product product, Long id) {
        return productRepository.findById(id).map(tempProduct -> {
            tempProduct.setName(product.getName());
            tempProduct.setDescription(product.getDescription());
            tempProduct.setUnitPrice(product.getUnitPrice());
            tempProduct.setImageUrl(product.getImageUrl());
            return productRepository.save(tempProduct);
        }).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}
