package guru.springframework.spring6restmvc.mappers;

import org.mapstruct.Mapper;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerDTO;

@Mapper
public interface BeerMapper {

	Beer beerDtoToBeer(BeerDTO beerDto);

	BeerDTO beerToBeerDto(Beer beer);
}
