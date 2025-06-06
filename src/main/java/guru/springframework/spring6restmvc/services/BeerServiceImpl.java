package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.BeerDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

	private Map<UUID, BeerDTO> beerMap;

	public BeerServiceImpl() {
        this.beerMap = new HashMap<>();
    }

	@Override
	public List<BeerDTO> listBeers() {

		log.debug("List beers in service");

		return beerMap.values().stream().toList();
	}

	@Override
	public Optional<BeerDTO> getBeerById(UUID id) {

		log.debug("Get Beer by Id in service. Id: " + id);

		return Optional.of(beerMap.get(id));
	}

	@Override
	public BeerDTO saveNewBeer(BeerDTO beer) {
		log.debug("Handle post in service");
		BeerDTO savedBeer = BeerDTO.builder()
				.id(UUID.randomUUID())
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.beerName(beer.getBeerName())
				.beerStyle(beer.getBeerStyle())
				.quantityOnHand(beer.getQuantityOnHand())
				.upc(beer.getUpc())
				.price(beer.getPrice())
				.build();

		this.beerMap.put(savedBeer.getId(), savedBeer);

		return savedBeer;
	}

	@Override
	public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
		log.debug("Update by Id in service. Id: " + beerId + ", Beer: " + beer);

		BeerDTO existingBeer = beerMap.get(beerId);
		if (existingBeer == null) {
			return Optional.empty();
		}
		existingBeer.setBeerName(beer.getBeerName());
		existingBeer.setBeerStyle(beer.getBeerStyle());
		existingBeer.setPrice(beer.getPrice());
		existingBeer.setUpc(beer.getUpc());
		existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
		existingBeer.setVersion(existingBeer.getVersion() + 1);
		existingBeer.setUpdateDate(LocalDateTime.now());

		return Optional.of(existingBeer);
	}

	@Override
	public Boolean deleteBeerById(UUID beerId) {
		log.debug("Delete by Id in service. Id: " + beerId);

		return this.beerMap.remove(beerId) != null;
	}

	@Override
	public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
		log.debug("Update Patch by Id in service. Id: " + beerId + ", Beer: " + beer);

		BeerDTO existingBeer = beerMap.get(beerId);
		if (existingBeer == null) {
			return Optional.empty();
		}

		if (beer.getBeerName() != null && !beer.getBeerName().isBlank()) {
			existingBeer.setBeerName(beer.getBeerName());
		}

		if (beer.getBeerStyle() != null) {
			existingBeer.setBeerStyle(beer.getBeerStyle());
		}

		if (beer.getPrice() != null) {
			existingBeer.setPrice(beer.getPrice());
		}

		if (beer.getQuantityOnHand() != null) {
			existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
		}

		if (beer.getUpc() != null && !beer.getUpc().isBlank()) {
			existingBeer.setUpc(beer.getUpc());
		}
		return Optional.of(existingBeer);
	}

}
