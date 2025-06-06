package guru.springframework.spring6restmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;

@DataJpaTest
class BootstrapDataTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CustomerRepository customerRepository;

	BootstrapData bootstrapData;

	@BeforeEach
	void setUp() throws Exception {
		bootstrapData = new BootstrapData(beerRepository, customerRepository);
//		// Clear existing data before running tests
//		beerRepository.deleteAll();
//		customerRepository.deleteAll();
//		// Load initial data
//		bootstrapData.run();
	}

	@Test
	void testRun() throws Exception {
		bootstrapData.run("");

		assertThat(beerRepository.count()).isEqualTo(3);
		assertThat(customerRepository.count()).isEqualTo(3);
	}

}
