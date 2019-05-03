package payroll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OrderController {

  private final OrderRepository orderRepository;
  private final OrderResourceAssembler assembler;

  /**
   * Inject the Order repository and assembler
   */
  OrderController(OrderRepository orderRepository, OrderResourceAssembler assembler) {
    this.orderRepository = orderRepository;
    this.assembler = assembler;
  }

  /**
   * Get all Order records
   * @return Resources list of Orders
   */
  @GetMapping("/orders")
  Resources<Resource<Order>> all() {
    List<Resource<Order>> orders = orderRepository.findAll().stream()
    .map(assembler::toResource)
    .collect(Collectors.toList());

    return new Resources<>(orders,
      linkTo(methodOn(OrderController.class).all()).withSelfRel());
  }

  /**
   * Get Order by Id
   * @param id
   * @return Order resource
   */
  @GetMapping("/orders/{id}")
  Resource<Order> one(@PathVariable Long id) {
    return assembler.toResource(
      orderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id)));
  }

  /**
   * Add new Order record
   * @param order
   * @return Order resource
   */
  @PostMapping("/orders")
  ResponseEntity<Resource<Order>> newOrder(@RequestBody Order order) {
    order.setStatus(Status.IN_PROGRESS);
    Order newOrder = orderRepository.save(order);

    return ResponseEntity
      .created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
      .body(assembler.toResource(newOrder));
  }

  /**
   * Change the state of an Order resource if the status is still IN_PROGRESS
   * @param id
   * @return ResponseEntity
   */
  @DeleteMapping("/orders/{id}/cancel")
  ResponseEntity<ResourceSupport> cancel(@PathVariable Long id) {
    Order order = orderRepository.findById(id)
      .orElseThrow(() -> new OrderNotFoundException(id));

    // Check if the Order is in the proper state to be changed
    if (order.getStatus() == Status.IN_PROGRESS) {
      // Set the status of the Order to cancel
      order.setStatus(Status.CANCELLED);
      return ResponseEntity.ok(assembler.toResource(orderRepository.save(order)));
    }

    // Handle case where the state of the Order is not in the correct state
    return ResponseEntity
      .status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status!"));
  }

  /**
   * Replace an Order resource if the state is still IN_PROGRESS
   * @param id
   * @return ResponseEntity
   */
  @PutMapping("/orders/{id}/complete")
  ResponseEntity<ResourceSupport> complete(@PathVariable Long id) {
    Order order = orderRepository.findById(id)
      .orElseThrow(() -> new OrderNotFoundException(id));

    // Check if the Order is in the proper state to be changed
    if (order.getStatus() == Status.IN_PROGRESS) {
      // Set the status of the Order to completed
      order.setStatus(Status.COMPLETED);
      return ResponseEntity.ok(assembler.toResource(orderRepository.save(order)));
    }

    // Handle case where the state of the Order is not in the correct state
    return ResponseEntity
      .status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status!"));
  }
}