package guru.springframework.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.entities.Customer;

@DataJpaTest
class CustomerRepositoryTest {

	@Autowired
	CustomerRepository customerRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSaveCustomer() {
		var savedCustomer = customerRepository.save(Customer.builder()
				.name("New Name")
				.build());
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isNotNull();
	}

}
