package com.szymonszopinski.ordermngmntsystem;

import com.szymonszopinski.ordermngmntsystem.exception.GlobalExceptionHandler;
import com.szymonszopinski.ordermngmntsystem.exception.OrderNotFoundException;
import com.szymonszopinski.ordermngmntsystem.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionClassesTest {

 @Test
 void testOrderNotFoundException() {
  String message = "Order not found";
  OrderNotFoundException exception = new OrderNotFoundException(message);

  assertEquals(message, exception.getMessage());
 }

 @Test
 void testProductNotFoundException() {
  String message = "Product not found";
  ProductNotFoundException exception = new ProductNotFoundException(message);

  assertEquals(message, exception.getMessage());
 }

 @Test
 void testHandleValidationExceptions() {
  GlobalExceptionHandler handler = new GlobalExceptionHandler();
  MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

  when(ex.getBindingResult()).thenReturn(new org.springframework.validation.BeanPropertyBindingResult(new Object(), "objectName"));

  Map<String, String> response = handler.handleValidationExceptions(ex);

  assertNotNull(response);
  assertTrue(response.isEmpty(), "Expected the response to be empty because no field errors were added.");
 }

 @Test
 void testHandleOrderNotFoundException() {
  GlobalExceptionHandler handler = new GlobalExceptionHandler();
  OrderNotFoundException ex = new OrderNotFoundException("Order not found");

  Map<String, String> response = handler.handleOrderNotFoundException(ex);

  assertNotNull(response);
  assertEquals("Order not found", response.get("error"));
 }

 @Test
 void testHandleProductNotFoundException() {
  GlobalExceptionHandler handler = new GlobalExceptionHandler();
  ProductNotFoundException ex = new ProductNotFoundException("Product not found");

  Map<String, String> response = handler.handleProductNotFoundException(ex);

  assertNotNull(response);
  assertEquals("Product not found", response.get("error"));
 }

 @Test
 void testHandleAllUncaughtException() {
  GlobalExceptionHandler handler = new GlobalExceptionHandler();
  Exception ex = new Exception("Unexpected error");

  Map<String, String> response = handler.handleAllUncaughtException(ex);

  assertNotNull(response);
  assertEquals("An unexpected error occurred", response.get("error"));
 }
}
