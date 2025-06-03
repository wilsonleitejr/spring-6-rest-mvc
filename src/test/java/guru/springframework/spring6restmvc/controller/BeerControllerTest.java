package guru.springframework.spring6restmvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;


@WebMvcTest(BeerController.class)
class BeerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	BeerService beerService;

	@Captor
	ArgumentCaptor<Beer> beerCaptor;

	BeerServiceImpl beerServiceImpl;

	@BeforeEach
	void beforeEach() {
		beerServiceImpl = new BeerServiceImpl();
	}

	@Test
	void testPatchBeerById() throws Exception {
		var testBeerId = UUID.randomUUID();
		var testBeer = Map.of("beerName", "New name");

		mockMvc.perform(patch(BeerController.BEER_ID_PATH, testBeerId)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testBeer)))
		.andExpect(status().isNoContent());

		verify(beerService).patchBeerById(eq(testBeerId), beerCaptor.capture());

		assertThat(beerCaptor.getValue().getBeerName()).isEqualTo(testBeer.get("beerName"));
	}


	@Test
	void testDeleteBeerById() throws Exception {
		var testBeerId = UUID.randomUUID();
		mockMvc.perform(delete(BeerController.BEER_ID_PATH, testBeerId))
				.andExpect(status().isNoContent());

		verify(beerService).deleteBeerById(testBeerId);
	}

	@Test
	void testUpdateBeerById() throws Exception {
		var testBeer = Beer.builder()
				.id(UUID.randomUUID())
				.beerName("Heineken")
				.beerStyle(BeerStyle.LAGGER)
				.price(BigDecimal.valueOf(10))
				.quantityOnHand(1)
				.upc("100")
				.build();

		mockMvc.perform(put(BeerController.BEER_ID_PATH, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testBeer)))
		.andExpect(status().isNoContent());

		verify(beerService).updateBeerById(testBeer.getId(), testBeer);
	}

	@Test
	void testCreateNewBeer() throws Exception {
		var savedBeer = Beer.builder()
				.id(UUID.randomUUID())
				.build();
		given(beerService.saveNewBeer(any(Beer.class))).willReturn(savedBeer);

		var beer = Beer.builder()
				.beerName("Heineken")
				.beerStyle(BeerStyle.LAGGER)
				.price(BigDecimal.valueOf(10))
				.quantityOnHand(1)
				.upc("100")
				.build();

		mockMvc.perform(post(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer)))
		.andExpect(status().isCreated())
		.andExpect(header().exists("Location"));
	}

	@Test
	void testListBeers() throws Exception {
		given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

		mockMvc.perform(get(BeerController.BEER_PATH)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(3)));
	}

	@Test
	void testGetByIdNotFound() throws Exception {
		var testBeerId = UUID.randomUUID();

		given(beerService.getBeerById(testBeerId)).willReturn(Optional.empty());

		mockMvc.perform(get(BeerController.BEER_ID_PATH, testBeerId)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetBeerById() throws Exception {
		var testBeer = beerServiceImpl.listBeers().get(0);
		given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

		mockMvc.perform(get(BeerController.BEER_ID_PATH, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
				.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())))
				.andExpect(jsonPath("$.beerStyle", is(testBeer.getBeerStyle().name())));
	}
}
