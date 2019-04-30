package payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class EmployeeNotFoundAdvice {

  /**
   * @ResponseBody:
   * Signals that this advice is rendered straight into the response body.
   */
  @ResponseBody

  /**
   * @ExceptionHandler:
   * Configures the advice to only respond if an EmployeeNotFoundException is thrown.
   */
  @ExceptionHandler(EmployeeNotFoundException.class)

  /**
   * @ResponseStatus:
   * Issues an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String employeeNotFoundHandler(EmployeeNotFoundException ex) {
    return ex.getMessage();
  }
}