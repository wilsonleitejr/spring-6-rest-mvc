package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

	public static final String CUSTOMER_PATH = "/api/v1/customer";
	public static final String CUSTOMER_ID_PATH = CUSTOMER_PATH + "/{customerId}";
	private final CustomerService customerService;

	@PatchMapping(CUSTOMER_ID_PATH)
	public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
		log.debug("Update Patch by Id in controller. Id: " + customerId + ", Customer: " + customer);

		customerService.patchCustomerById(customerId, customer);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(CUSTOMER_ID_PATH)
	public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {
		log.debug("Delete by Id in controller. Id: " + customerId);

		customerService.deleteCustomerById(customerId);

		return ResponseEntity.noContent().build();
	}

	@PutMapping(CUSTOMER_ID_PATH)
	public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
		log.debug("Update by Id in controller. Id: " + customerId + ", Customer: " + customer);

		customerService.updateCustomerById(customerId, customer);

		return ResponseEntity.noContent().build();
	}

	@PostMapping(CUSTOMER_PATH)
	public ResponseEntity handlePost(@RequestBody Customer customer) {
		log.debug("Handle post in controller. Customer: " + customer);

		Customer savedCustomer = customerService.saveNewCustomer(customer);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/customer/" + savedCustomer.getId());

		return ResponseEntity.created(null).headers(headers).build();
	}

	@GetMapping(CUSTOMER_PATH)
	public List<Customer> listConsumers() {
		log.debug("Getting all customers");
		return customerService.getAllCustomers();
	}

	@GetMapping(CUSTOMER_ID_PATH)
	public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
		log.debug("Get Customer by Id in controller. Id: " + customerId);
		return customerService.getCustomerById(customerId)
				.orElseThrow(NotFoundException::new);
	}
}
