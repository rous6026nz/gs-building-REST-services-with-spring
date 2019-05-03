package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderRepository:
 * This interface extends Spring Data JPA’s JpaRepository, specifying the
 * domain type as Order and the id type as Long and provides support for:
 * - Creating new instances
 * - Updating existing instances
 * - Deleting instances
 * - Finding (one, all, by simple or complex properties)
 */
public interface OrderRepository extends JpaRepository<Order, Long> {}