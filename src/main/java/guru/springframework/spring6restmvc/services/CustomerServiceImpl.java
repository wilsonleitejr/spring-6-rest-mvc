package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	private final Map<UUID, Customer> customerMap;

	public CustomerServiceImpl() {
		this.customerMap = new HashMap<>();

		Customer customer1 = Customer.builder()
				.id(UUID.randomUUID())
				.name("Customer 1")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		Customer customer2 = Customer.builder()
				.id(UUID.randomUUID())
				.name("Customer 2")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		Customer customer3 = Customer.builder()
				.id(UUID.randomUUID())
				.name("Customer 3")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		customerMap.put(customer1.getId(), customer1);
		customerMap.put(customer2.getId(), customer2);
		customerMap.put(customer3.getId(), customer3);
	}

	@Override
	public Optional<Customer> getCustomerById(UUID id) {
		return Optional.of(this.customerMap.get(id));
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerMap.values()
				.stream()
				.toList();
	}

	@Override
	public Customer saveNewCustomer(Customer customer) {
		log.debug("Handle post in service");
		Customer savedCustomer = Customer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.name(customer.getName())
				.build();

		this.customerMap.put(savedCustomer.getId(), savedCustomer);

		return savedCustomer;
	}

	@Override
	public void updateCustomerById(UUID customerId, Customer customer) {
		log.debug("Update by Id in service. Id: " + customerId + ", Customer: " + customer);

		Customer existingCustomer = customerMap.get(customerId);
		existingCustomer.setName(customer.getName());
		existingCustomer.setVersion(existingCustomer.getVersion() + 1);
		existingCustomer.setUpdateDate(LocalDateTime.now());
	}

	@Override
	public void deleteCustomerById(UUID customerId) {
		log.debug("Delete by Id in service. Id: " + customerId);

		this.customerMap.remove(customerId);

		log.debug("Customer with id " + customerId + " deleted");
	}

	@Override
	public void patchCustomerById(UUID customerId, Customer customer) {
		log.debug("Patch by Id in service. Id: " + customerId + ", Customer: " + customer);

		Customer existingCustomer = customerMap.get(customerId);

		if (customer.getName() != null && !customer.getName().isBlank()) {
			existingCustomer.setName(customer.getName());
		}
	}

}
