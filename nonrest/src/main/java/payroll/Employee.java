package payroll;

/**
 * @Data:
 * Lombok annotation to create all the getters, setters,
 * equals, hash, and toString methods, based on the fields.
 */
import lombok.Data;

/**
 * @Entity:
 * JPA annotation to make this object ready for storage in a
 * JPA-based data store.
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class Employee {

  private @Id @GeneratedValue Long id;
  public String name;
  public String role;

  Employee() {}

  /**
   * Custom constructor is created when we need to
   * create a new instance, but don’t yet have an id.
   */
  Employee(String name, String role) {
    this.name = name;
    this.role = role;
  }
}
