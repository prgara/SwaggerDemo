package io.pragra.SwaggerDemo.service;

import io.pragra.SwaggerDemo.model.Order;
import io.pragra.SwaggerDemo.model.Payment;
import io.pragra.SwaggerDemo.repository.OrderRepository;
import io.pragra.SwaggerDemo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // Create Order and Process Payment with Transactional Management
   // @Transactional(propagation = Propagation.REQUIRED)  // Both operations happen in the same transaction
    public Order createOrder(Order order) {
        // Create and save order (will be part of a transaction)
        Order savedOrder = orderRepository.save(order);

        // Process payment (will also be part of the same transaction)
        processPayment(savedOrder);

        return savedOrder;
    }

    // Process Payment (This will be part of the transaction started in createOrder())
   // @Transactional(propagation = Propagation.REQUIRES_NEW) // Starts a new transaction for payment
    public void processPayment(Order order) {
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice()); // Payment amount equals order price

        // Simulate a failure during payment processing
        if (order.getPrice() > 1000) {
            throw new RuntimeException("Payment processing failed");
        }

        paymentRepository.save(payment);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get Order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Update Order
    public Order updateOrder(Long id, Order order) {
        Optional<Order> existingOrder = getOrderById(id);
        if (existingOrder.isPresent()) {
            Order updatedOrder = existingOrder.get();
            updatedOrder.setProductName(order.getProductName());
            updatedOrder.setPrice(order.getPrice());
            return orderRepository.save(updatedOrder);
        }
        return null;  // Order not found
    }

    // Delete Order
    public boolean deleteOrder(Long id) {
        Optional<Order> existingOrder = getOrderById(id);
        if (existingOrder.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;  // Order not found
    }
}
