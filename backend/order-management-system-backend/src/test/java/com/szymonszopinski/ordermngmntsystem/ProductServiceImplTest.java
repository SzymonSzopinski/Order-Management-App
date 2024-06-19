package com.szymonszopinski.ordermngmntsystem;

import com.szymonszopinski.ordermngmntsystem.entity.Product;
import com.szymonszopinski.ordermngmntsystem.exception.ProductNotFoundException;
import com.szymonszopinski.ordermngmntsystem.repository.ProductRepository;
import com.szymonszopinski.ordermngmntsystem.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setUnitPrice(BigDecimal.valueOf(10.00));
        product1.setImageUrl("image1.jpg");

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setUnitPrice(BigDecimal.valueOf(20.00));
        product2.setImageUrl("image2.jpg");
    }

    @Test
    void testAddProduct() {
        when(productRepository.save(product1)).thenReturn(product1);

        Product savedProduct = productService.addProduct(product1);

        assertEquals(product1, savedProduct, "Saved product should match the input product");
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testGetProducts() {
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();

        assertEquals(2, result.size(), "Product list size should be 2");
        assertEquals(product1, result.get(0), "First product should be product1");
        assertEquals(product2, result.get(1), "Second product should be product2");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setUnitPrice(BigDecimal.valueOf(30.00));
        updatedProduct.setImageUrl("updatedImage.jpg");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(updatedProduct, 1L);

        assertEquals("Updated Product", result.getName(), "Product name should be updated");
        assertEquals("Updated Description", result.getDescription(), "Product description should be updated");
        assertEquals(BigDecimal.valueOf(30.00), result.getUnitPrice(), "Product price should be updated");
        assertEquals("updatedImage.jpg", result.getImageUrl(), "Product image URL should be updated");
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product result = productService.getProductById(1L);

        assertEquals(product1, result, "Product retrieved by ID should match product1");
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L), "Expected ProductNotFoundException when product is not found");
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L), "Expected ProductNotFoundException when deleting non-existing product");
    }
}
