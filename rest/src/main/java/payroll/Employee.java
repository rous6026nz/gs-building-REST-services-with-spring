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
  private String firstName;
  private String lastName;
  private String role;

  Employee() {}

    /**
   * Custom constructor is created when we need to
   * create a new instance, but don’t yet have an id.
   */
  Employee(String firstName,String lastName, String role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
  }

  /**
   * Method: getName:
   * A "virtual" getter for the old name property, getName() is defined.
   * It uses the firstName and lastName fields to produce a value.
   *
   * @Return String - firstanme and lastName
   */
  public String getName() {
    return this.firstName + " " + this.lastName;
  }

  /**
   * Method: setName:
   * A "virtual" setter for the old name property is also defined, setName().
   * It parses an incoming string and stores it into the proper fields.
   *
   * @Params String name
   */
  public void setName(String name) {
    String[] parts = name.split(" ");
    this.firstName = parts[0];
    this.lastName = parts[1];
  }
}