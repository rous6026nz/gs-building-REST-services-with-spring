package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * EmployeeRepository:
 * This interface extends Spring Data JPA’s JpaRepository, specifying the
 * domain type as Employee and the id type as Long and provides support for:
 * - Creating new instances
 * - Updating existing instances
 * - Deleting instances
 * - Finding (one, all, by simple or complex properties)
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}