package guru.springframework.spring6restmvc.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

	private final BeerService beerService;

	@PatchMapping("{beerId}")
	public ResponseEntity patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
		log.debug("Update Patch by Id in controller. Id: " + beerId + ", Beer: " + beer);

		beerService.patchBeerById(beerId, beer);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("{beerId}")
	public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId) {
		log.debug("Delete by Id in controller. Id: " + beerId);

		beerService.deleteBeerById(beerId);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("{beerId}")
	public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
		log.debug("Update by Id in controller. Id: " + beerId + ", Beer: " + beer);

		beerService.updateBeerById(beerId, beer);

		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity handlePost(@RequestBody Beer beer) {
		log.debug("Handle post in controller. Beer: " + beer);

		Beer savedBeer = beerService.saveNewBeer(beer);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/beer/" + savedBeer.getId());

		return ResponseEntity.created(null).headers(headers).build();
	}

	@RequestMapping(method = GET)
	public List<Beer> listBeers() {
		log.debug("List beers in controller");
		return beerService.listBeers();
	}

	@RequestMapping(value = "/{beerId}", method = GET)
	public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
		log.debug("Get Beer by Id in controller. Id: " + beerId);
		return beerService.getBeerById(beerId);
	}

}
