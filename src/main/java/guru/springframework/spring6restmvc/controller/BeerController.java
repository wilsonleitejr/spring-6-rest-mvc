package guru.springframework.spring6restmvc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

	public static final String BEER_PATH = "/api/v1/beer";
	public static final String BEER_ID_PATH = BEER_PATH + "/{beerId}";

	private final BeerService beerService;

	@PatchMapping(BEER_ID_PATH)
	public ResponseEntity patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
		log.debug("Update Patch by Id in controller. Id: " + beerId + ", Beer: " + beer);

		beerService.patchBeerById(beerId, beer);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(BEER_ID_PATH)
	public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId) {
		log.debug("Delete by Id in controller. Id: " + beerId);

		beerService.deleteBeerById(beerId);

		return ResponseEntity.noContent().build();
	}

	@PutMapping(BEER_ID_PATH)
	public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
		log.debug("Update by Id in controller. Id: " + beerId + ", Beer: " + beer);

		beerService.updateBeerById(beerId, beer);

		return ResponseEntity.noContent().build();
	}

	@PostMapping(BEER_PATH)
	public ResponseEntity handlePost(@RequestBody Beer beer) {
		log.debug("Handle post in controller. Beer: " + beer);

		Beer savedBeer = beerService.saveNewBeer(beer);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/beer/" + savedBeer.getId());

		return ResponseEntity.created(null).headers(headers).build();
	}

	@GetMapping(BEER_PATH)
	public List<Beer> listBeers() {
		log.debug("List beers in controller");
		return beerService.listBeers();
	}

	@GetMapping(BEER_ID_PATH)
	public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
		log.debug("Get Beer by Id in controller. Id: " + beerId);
		return beerService.getBeerById(beerId)
				.orElseThrow(NotFoundException::new);
	}
}
