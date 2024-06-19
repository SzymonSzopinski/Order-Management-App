package com.szymonszopinski.ordermngmntsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szymonszopinski.ordermngmntsystem.controller.ProductController;
import com.szymonszopinski.ordermngmntsystem.entity.Product;
import com.szymonszopinski.ordermngmntsystem.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

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
        product1.setImageUrl("http://example.com/product1.jpg");

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setUnitPrice(BigDecimal.valueOf(20.00));
        product2.setImageUrl("http://example.com/product2.jpg");
    }

    @Test
    void testGetProducts() throws Exception {
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getProducts()).thenReturn(products);

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(product1.getName())))
                .andExpect(jsonPath("$[1].id", is(product2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(product2.getName())));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(product1.getId())).thenReturn(product1);

        mockMvc.perform(get("/products/{id}", product1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(product1.getName())));
    }

    @Test
    void testAddProduct() throws Exception {
        when(productService.addProduct(any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(product1.getName())));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(any(Product.class), eq(product1.getId()))).thenReturn(product1);

        mockMvc.perform(put("/products/{id}", product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(product1.getName())));
    }

    @Test
    void testDeleteProductById() throws Exception {
        doNothing().when(productService).deleteProduct(product1.getId());

        mockMvc.perform(delete("/products/{id}", product1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(product1.getId());
    }
}
