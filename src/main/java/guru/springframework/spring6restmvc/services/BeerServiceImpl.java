package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

	private Map<UUID, Beer> beerMap;

	public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

	@Override
	public List<Beer> listBeers() {

		log.debug("List beers in service");

		return beerMap.values().stream().toList();
	}

	@Override
	public Beer getBeerById(UUID id) {

		log.debug("Get Beer by Id in service. Id: " + id);

		return beerMap.get(id);
	}

	@Override
	public Beer saveNewBeer(Beer beer) {
		log.debug("Handle post in service");
		Beer savedBeer = Beer.builder()
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
	public void updateBeerById(UUID beerId, Beer beer) {
		log.debug("Update by Id in service. Id: " + beerId + ", Beer: " + beer);

		Beer existingBeer = beerMap.get(beerId);
		existingBeer.setBeerName(beer.getBeerName());
		existingBeer.setBeerStyle(beer.getBeerStyle());
		existingBeer.setPrice(beer.getPrice());
		existingBeer.setUpc(beer.getUpc());
		existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
		existingBeer.setVersion(existingBeer.getVersion() + 1);
		existingBeer.setUpdateDate(LocalDateTime.now());

	}

	@Override
	public void deleteBeerById(UUID beerId) {
		log.debug("Delete by Id in service. Id: " + beerId);

		this.beerMap.remove(beerId);

		log.debug("Beer with id " + beerId + " deleted");
	}

	@Override
	public void patchBeerById(UUID beerId, Beer beer) {
		log.debug("Update Patch by Id in service. Id: " + beerId + ", Beer: " + beer);

		Beer existingBeer = beerMap.get(beerId);

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
	}

}
