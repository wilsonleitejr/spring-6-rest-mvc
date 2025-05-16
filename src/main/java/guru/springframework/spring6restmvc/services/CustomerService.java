package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.Customer;

public interface CustomerService {

	Customer getCustomerById(UUID id);

	List<Customer> getAllCustomers();

	Customer saveNewCustomer(Customer customer);

	void updateCustomerById(UUID customerId, Customer customer);

	void deleteCustomerById(UUID customerId);

	void patchCustomerById(UUID customerId, Customer customer);
}
