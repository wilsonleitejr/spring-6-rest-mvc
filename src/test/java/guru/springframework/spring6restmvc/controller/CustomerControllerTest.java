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

import java.util.List;
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

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import guru.springframework.spring6restmvc.services.CustomerServiceImpl;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	CustomerService customerService;

	@Captor
	ArgumentCaptor<CustomerDTO> customerCaptor;

	CustomerServiceImpl customerServiceImpl;

	@BeforeEach
	void beforeEach() {
		customerServiceImpl = new CustomerServiceImpl();
	}

	@Test
	void testPatchCustomerById() throws Exception {
		var testCustomer = CustomerDTO.builder()
				.id(UUID.randomUUID())
				.name("Customer name")
				.build();

		given(customerService.patchCustomerById(testCustomer.getId(), testCustomer))
				.willReturn(Optional.of(testCustomer));

		mockMvc.perform(patch(CustomerController.CUSTOMER_ID_PATH, testCustomer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testCustomer)))
		.andExpect(status().isNoContent());

		verify(customerService).patchCustomerById(eq(testCustomer.getId()), customerCaptor.capture());

		assertThat(customerCaptor.getValue().getName()).isEqualTo(testCustomer.getName());
	}

	@Test
	void testDeleteCustomerById() throws Exception {
		var testCustomer = CustomerDTO.builder().id(UUID.randomUUID()).build();

		given(customerService.deleteCustomerById(testCustomer.getId())).willReturn(true);

		mockMvc.perform(delete(CustomerController.CUSTOMER_ID_PATH, testCustomer.getId()))
		.andExpect(status().isNoContent());

		verify(customerService).deleteCustomerById(testCustomer.getId());

	}

	@Test
	void testUpdateCustomerById() throws Exception {
		var testCustomer = CustomerDTO.builder()
				.id(UUID.randomUUID())
				.name("Customer name updated")
				.build();

		given(customerService.updateCustomerById(testCustomer.getId(), testCustomer)).willReturn(Optional.of(testCustomer));

		mockMvc.perform(put(CustomerController.CUSTOMER_ID_PATH, testCustomer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testCustomer)))
		.andExpect(status().isNoContent());

		verify(customerService).updateCustomerById(testCustomer.getId(), testCustomer);
	}

	@Test
	void testCreateNewCustomer() throws Exception {
		var customerSaved = CustomerDTO.builder()
				.id(UUID.randomUUID())
				.build();
		given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customerSaved);

		var customer = CustomerDTO.builder()
				.name("Customer 4")
				.build();


		mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))
				)
		.andExpect(status().isCreated())
		.andExpect(header().exists("Location"));
	}

	@Test
	void testListConsumers() throws Exception {
		var customers = List.of(
				CustomerDTO.builder().id(UUID.randomUUID()).name("Customer 1").build(),
				CustomerDTO.builder().id(UUID.randomUUID()).name("Customer 2").build(),
				CustomerDTO.builder().id(UUID.randomUUID()).name("Customer 3").build()
		);
		given(customerService.getAllCustomers()).willReturn(customers);

		mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()", is(3)));
	}

	@Test
	void testGetCustomerByIdNotFound() throws Exception {
		var testCustomerId = UUID.randomUUID();
		given(customerService.getCustomerById(testCustomerId)).willReturn(Optional.empty());

		mockMvc.perform(get(CustomerController.CUSTOMER_ID_PATH, testCustomerId)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Test
	void testGetCustomerById() throws Exception {
		var testCustomer = CustomerDTO.builder()
				.id(UUID.randomUUID())
				.name("Customer 1")
				.build();

		given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

		mockMvc.perform(get(CustomerController.CUSTOMER_ID_PATH, testCustomer.getId())
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
		.andExpect(jsonPath("$.name", is(testCustomer.getName())));
	}

}
