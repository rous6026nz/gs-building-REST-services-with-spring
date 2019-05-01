package payroll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {

  private final EmployeeRepository repository;
  private final EmployeeResourceAssembler assembler;

  /**
   * Inject the Employee repository and assembler.
   */
  EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
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
      .map(assembler::toResource)
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

    return assembler.toResource(employee);
  }

  // Add new employee record
  @PostMapping("/employees")
  ResponseEntity newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
    /**
     * The new Employee object is saved as before, but is wrapped using the EmployeeResourceAssembler.
     */
    Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));
    /**
     * ResponseEntity is used to create an HTTP 201 Created status message. This type of response typically
     * includes a Location response header, and we use the newly formed link to return the resource-based
     * version of the saved object.
     */
    return ResponseEntity
      .created(new URI(resource.getId().expand().getHref()))
      .body(resource);
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
