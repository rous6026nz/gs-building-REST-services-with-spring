package payroll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {

  public final EmployeeRepository repository;

  EmployeeController(EmployeeRepository repository) {
    this.repository = repository;
  }

  // Aggregate root
  @GetMapping("/employees")

  /**
   * Resources:
   * A Spring HATEOAS container aimed at encapsulating collections of employee resources,
   * and also provides the functionality to include links.
   */
  Resources<Resource<Employee>> all() {

    /**
     * Fetchs all the employees, but then transform them into a list of Resource<Employee> objects
     * using the Java stream API.
     */
    List<Resource<Employee>> employees = repository.findAll().stream()
      .map(employee -> new Resource<>(employee,
        /**
         * Builds a link to the EmployeeController's one() method, and flag it as a self link.
         */
        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
        /**
         * Builds a link to the aggregate root, all(), and call it "employees".
         */
        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
      .collect(Collectors.toList());

    return new Resources<>(employees,
      /**
       * Builds a link to the aggregate root top-level.
       */
      linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  /**
   * Get employee by ID:
   * @Return Resource<Employee> - a generic container that includes not only the data but a collection of links.
   */
  @GetMapping("/employees/{id}")
  Resource<Employee> one(@PathVariable Long id) {
    Employee employee = repository.findById(id)
      .orElseThrow(() -> new EmployeeNotFoundException(id));

    return new Resource<>(employee,
      /**
       * Builds a link to the EmployeeController's one() method, and flag it as a self link.
       */
      linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
      /**
       * Builds a link to the aggregate root, all(), and call it "employees".
       */
      linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
  }

  // Add new employee record
  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  // Update employee record
  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
    return repository.findById(id)
      .map(employee -> {
        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());
        return repository.save(employee);
      })
      .orElseGet(() -> {
        newEmployee.setId(id);
        return repository.save(newEmployee);
      });
  }

  // Delete employee record
  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
