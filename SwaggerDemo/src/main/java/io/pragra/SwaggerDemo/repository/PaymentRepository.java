package io.pragra.SwaggerDemo.repository;

import io.pragra.SwaggerDemo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Custom query methods if needed
}
