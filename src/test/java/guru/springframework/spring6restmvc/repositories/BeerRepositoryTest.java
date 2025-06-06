package guru.springframework.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.entities.Beer;

@DataJpaTest
class BeerRepositoryTest {

	@Autowired
	BeerRepository beerRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSaveBeer() {
		var savedBeer = beerRepository.save(Beer.builder()
				.beerName("My Beer")
				.build());
		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
	}

}
