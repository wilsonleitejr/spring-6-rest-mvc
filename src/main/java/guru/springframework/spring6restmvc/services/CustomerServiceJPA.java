package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Primary
@Service
public class CustomerServiceJPA implements CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	@Override
	public Optional<CustomerDTO> getCustomerById(UUID id) {
		return Optional.ofNullable(customerRepository.findById(id)
				.map(customerMapper::customerToCustomerDto)
				.orElse(null));
	}

	@Override
	public List<CustomerDTO> getAllCustomers() {
		return customerRepository.findAll()
				.stream()
				.map(customerMapper::customerToCustomerDto)
				.toList();
	}

	@Override
	public CustomerDTO saveNewCustomer(CustomerDTO customer) {
		return customerMapper.customerToCustomerDto(
				customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
	}

	@Override
	public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
		var opCustomer = customerRepository.findById(customerId);

		if (opCustomer.isEmpty()) {
			return Optional.empty();
		}

		var existingCustomer = opCustomer.get();

		existingCustomer.setName(customer.getName());
		existingCustomer.setUpdateDate(LocalDateTime.now());

		//var savedCustomer = customerRepository.save(existingCustomer);

		return Optional.of(customerMapper.customerToCustomerDto(existingCustomer));
	}

	@Override
	public Boolean deleteCustomerById(UUID customerId) {
		if (!customerRepository.existsById(customerId)) {
			return false;
		}

		customerRepository.deleteById(customerId);
		return true;
	}

	@Override
	public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
		var opCustomer = customerRepository.findById(customerId);

		if (opCustomer.isEmpty()) {
			return Optional.empty();
		}

		var existingCustomer = opCustomer.get();

		if (customer.getName() != null) {
			existingCustomer.setName(customer.getName());
		}

		return Optional.of(customerMapper.customerToCustomerDto(existingCustomer));
	}

}
