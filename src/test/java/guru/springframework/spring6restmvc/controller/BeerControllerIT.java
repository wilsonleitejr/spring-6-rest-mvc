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

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;

@SpringBootTest
class BeerControllerIT {

	@Autowired
	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	BeerMapper beerMapper;

	@Test
	void testPatchIdNotFound() {
		var beerDTO = BeerDTO.builder()
				.beerName("Test beer")
				.build();

		assertThrows(NotFoundException.class, () -> {
			beerController.patchBeerById(UUID.randomUUID(), beerDTO);
		});
	}

	@Rollback
	@Transactional
	@Test
	void testPatchCustomerById() {
		var beer = beerRepository.findAll().get(0);
		var beerDTO = beerMapper.beerToBeerDto(beer);
		beerDTO.setId(null);
		beerDTO.setVersion(null);
		final String beerName = "Patched Beer Name";
		beerDTO.setBeerName(beerName);

		var responseEntity = beerController.patchBeerById(beer.getId(), beerDTO);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

		var patchedCustomer = beerController.getBeerById(beer.getId());

		assertThat(patchedCustomer).isNotNull();
		assertThat(patchedCustomer.getId()).isEqualTo(beer.getId());
		assertThat(patchedCustomer.getBeerName()).isEqualTo(beerName);
	}

	@Test
	void testDeleteByIdNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.deleteBeerById(UUID.randomUUID());
		});
	}

	@Rollback
	@Transactional
	@Test
	void testDeleteById() {
		var beer = beerRepository.findAll().get(0);

		var responseEntity = beerController.deleteBeerById(beer.getId());

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

		assertThat(beerRepository.findById(beer.getId())).isEmpty();
	}


	@Test
	void testUpdateIdNotFound() {
		var beerDTO = BeerDTO.builder()
				.beerName("Test Beer")
				.build();

		assertThrows(NotFoundException.class, () -> {
			beerController.updateBeerById(UUID.randomUUID(), beerDTO);
		});
	}

	@Rollback
	@Transactional
	@Test
	void testUpdateBeerById() {
		var beer = beerRepository.findAll().get(0);
		var beerDTO = beerMapper.beerToBeerDto(beer);
		beerDTO.setId(null);
		beerDTO.setVersion(null);
		final String beerName = "Updated Beer Name";
		beerDTO.setBeerName(beerName);

		var responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.NO_CONTENT.value());

		var updatedBeer = beerController.getBeerById(beer.getId());

		assertThat(updatedBeer).isNotNull();
		assertThat(updatedBeer.getId()).isEqualTo(beer.getId());
		assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
	}

	@Rollback
	@Transactional
	@Test
	void testSaveNew() {
		BeerDTO newBeer = BeerDTO.builder()
				.beerName("New Beer")
				.build();

		var responseEntity = beerController.handlePost(newBeer);

		assertThat(responseEntity).isNotNull();
		assertThat(responseEntity.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

		String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
		assertThat(locationUUID.length).isEqualTo(5);
		UUID savedUUID = UUID.fromString(locationUUID[4]);

		var savedBeer = beerController.getBeerById(savedUUID);
		assertThat(savedBeer).isNotNull();
	}

	@Test
	void testGetByIdNotFound() {
		assertThrows(NotFoundException.class, () -> {
			beerController.getBeerById(UUID.randomUUID());
		});
	}

	@Test
	void testGetById() {
		var beer = beerRepository.findAll().get(0);

		var dto = beerController.getBeerById(beer.getId());

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(beer.getId());
	}

	@Test
	void testListAll() {
		var dtos = beerController.listBeers();

		assertThat(dtos).isNotNull();
		assertThat(dtos.size()).isEqualTo(3);
	}

	@Rollback
	@Transactional
	@Test
	void testEmptyList() {
		beerRepository.deleteAll();
		var dtos = beerController.listBeers();

		assertThat(dtos).isNotNull();
		assertThat(dtos.size()).isEqualTo(0);
	}
}
