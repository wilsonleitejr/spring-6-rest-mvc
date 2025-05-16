package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.Beer;

public interface BeerService {

	Beer getBeerById(UUID id);

	List<Beer> listBeers();

	Beer saveNewBeer(Beer beer);

	void updateBeerById(UUID beerId, Beer beer);

	void deleteBeerById(UUID beerId);

	void patchBeerById(UUID beerId, Beer beer);
}
