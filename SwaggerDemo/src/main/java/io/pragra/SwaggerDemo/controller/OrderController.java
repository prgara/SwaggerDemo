package io.pragra.SwaggerDemo.controller;

import io.pragra.SwaggerDemo.model.Order;
import io.pragra.SwaggerDemo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Cacheable for create operation isn't necessary as it would only apply for read operations, but you can have it for the result.

    // Create Order
    @Operation(summary = "Create Order", description = "API to create Order", tags = "Create")
    @ApiResponse(responseCode = "201", description = "Order has been created")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    // Cache Get All Orders (Optional depending on data size)
    @GetMapping
    @Cacheable(value = "ordersCache")  // Cache all orders, assuming they don't change frequently.
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get Order by ID (Cache the result of individual order fetch)
    @GetMapping("/{id}")
    @Cacheable(value = "ordersCache", key = "#id")  // Cache individual orders by ID
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update Order
    @PutMapping("/{id}")
    @CacheEvict(value = "ordersCache", key = "#id")  // Evict the cached order by ID before updating
    @CachePut(value = "ordersCache", key = "#id")  // Re-cache the updated order
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
    }

    // Delete Order
    @DeleteMapping("/{id}")
    @CacheEvict(value = "ordersCache", key = "#id")  // Evict the cache entry for the deleted order
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        boolean isDeleted = orderService.deleteOrder(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
