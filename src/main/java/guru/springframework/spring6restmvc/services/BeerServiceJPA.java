package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Primary
@Service
public class BeerServiceJPA implements BeerService {

	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;

	@Override
	public Optional<BeerDTO> getBeerById(UUID id) {
		return Optional.ofNullable(beerRepository.findById(id)
				.map(beerMapper::beerToBeerDto)
				.orElse(null));
	}

	@Override
	public List<BeerDTO> listBeers() {
		return beerRepository.findAll()
				.stream()
				.map(beerMapper::beerToBeerDto)
				.toList();
	}

	@Override
	public BeerDTO saveNewBeer(BeerDTO beer) {
		return beerMapper.beerToBeerDto(
				beerRepository.save(beerMapper.beerDtoToBeer(beer))
		);
	}

	@Override
	public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
		var opBeer = beerRepository.findById(beerId);

		if (opBeer.isEmpty()) {
			return Optional.empty();
		}

		var existingBeer = opBeer.get();
		existingBeer.setBeerName(beer.getBeerName());
		existingBeer.setBeerStyle(beer.getBeerStyle());
		existingBeer.setUpc(beer.getUpc());
		existingBeer.setPrice(beer.getPrice());
		existingBeer.setUpdateDate(LocalDateTime.now());

		// Save the updated beer back to the repository
		//var savedBeer = beerRepository.save(existingBeer);

		return Optional.of(beerMapper.beerToBeerDto(existingBeer));
	}

	@Override
	public Boolean deleteBeerById(UUID beerId) {
		if (!beerRepository.existsById(beerId)) {
			return false;
		}

		beerRepository.deleteById(beerId);
		return true;
	}

	@Override
	public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beerDto) {
		var opBeer = beerRepository.findById(beerId);

		if (opBeer.isEmpty()) {
			return Optional.empty();
		}

		var existingBeer = opBeer.get();
		// Update only the fields that are not null in the DTO
		if (beerDto.getBeerName() != null) {
			existingBeer.setBeerName(beerDto.getBeerName());
		}
		if (beerDto.getBeerStyle() != null) {
			existingBeer.setBeerStyle(beerDto.getBeerStyle());
		}
		if (beerDto.getUpc() != null) {
			existingBeer.setUpc(beerDto.getUpc());
		}
		if (beerDto.getQuantityOnHand() != null) {
			existingBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
		}
		if (beerDto.getPrice() != null) {
			existingBeer.setPrice(beerDto.getPrice());
		}

		beerRepository.save(existingBeer);

		return Optional.of(beerMapper.beerToBeerDto(existingBeer));
	}

}
