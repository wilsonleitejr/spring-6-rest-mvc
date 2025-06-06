package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	private final Map<UUID, CustomerDTO> customerMap;

	public CustomerServiceImpl() {
		this.customerMap = new HashMap<>();
	}

	@Override
	public Optional<CustomerDTO> getCustomerById(UUID id) {
		return Optional.of(this.customerMap.get(id));
	}

	@Override
	public List<CustomerDTO> getAllCustomers() {
		return customerMap.values()
				.stream()
				.toList();
	}

	@Override
	public CustomerDTO saveNewCustomer(CustomerDTO customer) {
		log.debug("Handle post in service");
		CustomerDTO savedCustomer = CustomerDTO.builder()
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
	public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
		log.debug("Update by Id in service. Id: " + customerId + ", Customer: " + customer);

		CustomerDTO existingCustomer = customerMap.get(customerId);
		if (existingCustomer == null) {
			log.debug("Customer with id " + customerId + " not found");
			return Optional.empty();
		}

		existingCustomer.setName(customer.getName());
		existingCustomer.setVersion(existingCustomer.getVersion() + 1);
		existingCustomer.setUpdateDate(LocalDateTime.now());

		return Optional.of(existingCustomer);
	}

	@Override
	public Boolean deleteCustomerById(UUID customerId) {
		log.debug("Delete by Id in service. Id: " + customerId);

		return this.customerMap.remove(customerId) != null;
	}

	@Override
	public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
		log.debug("Patch by Id in service. Id: " + customerId + ", Customer: " + customer);

		CustomerDTO existingCustomer = customerMap.get(customerId);
		if (existingCustomer == null) {
			log.debug("Customer with id " + customerId + " not found");
			return Optional.empty();
		}

		if (customer.getName() != null && !customer.getName().isBlank()) {
			existingCustomer.setName(customer.getName());
		}
		return Optional.of(existingCustomer);
	}

}
