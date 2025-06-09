package guru.springframework.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
class BeerRepositoryTest {

	@Autowired
	BeerRepository beerRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSaveBeerNameTooLong() {
		assertThrows(ConstraintViolationException.class, () -> {
			beerRepository.saveAndFlush(Beer.builder()
					.beerName(Stream.generate(() -> "A").limit(51).reduce("", String::concat))
					.beerStyle(BeerStyle.ALE)
					.upc("123456789012")
					.price(new BigDecimal("9.99"))
					.build());
		});
	}

	@Test
	void testSaveBeer() {
		var savedBeer = beerRepository.saveAndFlush(Beer.builder()
				.beerName("My Beer")
				.beerStyle(BeerStyle.ALE)
				.upc("123456789012")
				.price(new BigDecimal("9.99"))
				.build());
		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
	}

}
