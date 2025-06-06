package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;

@SpringBootTest
class CustomerControllerIT {

	@Autowired
	CustomerController customerController;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerMapper customerMapper;

	@Test
	void testPatchIdNotFound() {
		var customerDTO = CustomerDTO.builder()
				.name("Test customer")
				.build();

		assertThrows(NotFoundException.class, () -> {
			customerController.patchCustomerById(UUID.randomUUID(), customerDTO);
		});
	}

	@Rollback
	@Transactional
	@Test
	void testPatchCustomerById() {
		var customer = customerRepository.findAll().get(0);
		var customerDTO = customerMapper.customerToCustomerDto(customer);
		customerDTO.setId(null);
		customerDTO.setVersion(null);
		final String customerName = "Patched Customer Name";
		customerDTO.setName(customerName);

		var responseEntity = customerController.patchCustomerById(customer.getId(), customerDTO);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

		var patchedCustomer = customerController.getCustomerById(customer.getId());

		assertThat(patchedCustomer).isNotNull();
		assertThat(patchedCustomer.getId()).isEqualTo(customer.getId());
		assertThat(patchedCustomer.getName()).isEqualTo(customerName);
	}

	@Test
	void testDeleteByIdNotFound() {
		assertThrows(NotFoundException.class, () -> {
			customerController.deleteCustomerById(UUID.randomUUID());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testDeleteById() {
		var customer = customerRepository.findAll().get(0);

		var responseEntity = customerController.deleteCustomerById(customer.getId());

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

		assertThat(customerRepository.findById(customer.getId())).isEmpty();
	}

	@Test
	void testUpdateIdNotFound() {
		var customerDTO = CustomerDTO.builder()
				.name("Test customer")
				.build();

		assertThrows(NotFoundException.class, () -> {
			customerController.updateCustomerById(UUID.randomUUID(), customerDTO);
		});
	}

	@Rollback
	@Transactional
	@Test
	void testUpdateById() {
		var customer = customerRepository.findAll().get(0);
		var customerDTO = customerMapper.customerToCustomerDto(customer);
		customerDTO.setId(null);
		customerDTO.setVersion(null);
		final String customerName = "Updated Customer Name";
		customerDTO.setName(customerName);

		var responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

		var updatedCustomer = customerController.getCustomerById(customer.getId());

		assertThat(updatedCustomer).isNotNull();
		assertThat(updatedCustomer.getId()).isEqualTo(customer.getId());
		assertThat(updatedCustomer.getName()).isEqualTo(customerName);
	}

	@Rollback
	@Transactional
	@Test
	void testSaveNew() {
		CustomerDTO newCustomer = CustomerDTO.builder()
				.name("New Customer")
				.build();

		var responseEntity = customerController.handlePost(newCustomer);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

		String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
		assertThat(locationUUID.length).isEqualTo(5);
		UUID savedUUID = UUID.fromString(locationUUID[4]);

		var savedCustomer = customerController.getCustomerById(savedUUID);
		assertThat(savedCustomer).isNotNull();
	}

	@Test
	void testGetByIdNotFound() {
		assertThrows(NotFoundException.class, () -> {
			customerController.getCustomerById(UUID.randomUUID());
		});
	}

	@Test
	void testGetById() {
		var customer = customerRepository.findAll().get(0);

		var dto = customerController.getCustomerById(customer.getId());

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(customer.getId());
	}

	@Test
	void testListAll() {
		var dtos = customerController.listConsumers();

		assertThat(dtos).isNotNull();
		assertThat(dtos.size()).isEqualTo(3);
	}

	@Rollback
	@Transactional
	@Test
	void testEmptyList() {
		customerRepository.deleteAll();
		var dtos = customerController.listConsumers();

		assertThat(dtos).isNotNull();
		assertThat(dtos.size()).isEqualTo(0);
	}
}
