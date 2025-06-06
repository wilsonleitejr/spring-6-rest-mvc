package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.BeerDTO;

public interface BeerService {

	Optional<BeerDTO> getBeerById(UUID id);

	List<BeerDTO> listBeers();

	BeerDTO saveNewBeer(BeerDTO beer);

	Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

	Boolean deleteBeerById(UUID beerId);

	Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer);
}
