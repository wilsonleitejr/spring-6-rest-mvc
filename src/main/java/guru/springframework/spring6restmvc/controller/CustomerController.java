package guru.springframework.spring6restmvc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {

	private final CustomerService customerService;

	@PatchMapping("{customerId}")
	public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
		log.debug("Update Patch by Id in controller. Id: " + customerId + ", Customer: " + customer);

		customerService.patchCustomerById(customerId, customer);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("{customerId}")
	public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {
		log.debug("Delete by Id in controller. Id: " + customerId);

		customerService.deleteCustomerById(customerId);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{customerId}")
	public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
		log.debug("Update by Id in controller. Id: " + customerId + ", Customer: " + customer);

		customerService.updateCustomerById(customerId, customer);

		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity handlePost(@RequestBody Customer customer) {
		log.debug("Handle post in controller. Customer: " + customer);

		Customer savedCustomer = customerService.saveNewCustomer(customer);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/customer/" + savedCustomer.getId());

		return ResponseEntity.created(null).headers(headers).build();
	}

	@RequestMapping(method = GET)
	public List<Customer> listConsumers() {
		log.debug("Getting all customers");
		return customerService.getAllCustomers();
	}

	@RequestMapping(value = "/{customerId}", method = GET)
	public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
		log.debug("Get Customer by Id in controller. Id: " + customerId);
		return customerService.getCustomerById(customerId);
	}
}
