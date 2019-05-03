package payroll;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
    return args -> {
      employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
      employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));

      employeeRepository.findAll().forEach(employee -> {
        log.info("Preloading " + employee);
      });

      orderRepository.save(new Order("Macbook Pro", Status.COMPLETED));
      orderRepository.save(new Order("iPhone XR", Status.IN_PROGRESS));

      orderRepository.findAll().forEach(order -> {
        log.info("Preloading " + order);
      });
    };
  }
}