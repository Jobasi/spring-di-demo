package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import entities.Customer;
import helpers.CustomerBuilder;
import data.CustomerDAO;

public class CustomerService {
	private CustomerDAO customerDAO; 
	public CustomerService() {
		this.customerDAO = new CustomerDAO();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Customer findCustomer(@PathParam("id") String id){
		return customerDAO.findCustomerById(Long.valueOf(id));
	}
	
	@Path("/all")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Customer> getAllCustomers(){
		return customerDAO.fetchAllCustomers();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Customer createCustomer(JAXBElement<Customer> cus){
		
		Customer customer = new CustomerBuilder(cus.getValue().getFirstName(), cus.getValue().getLastName())
				.withEmail(cus.getValue().getEmail())
				.withPhone(cus.getValue().getPhoneNumber())
				.build();
		
		Long customerId = customerDAO.save(customer);
		
		return customerDAO.findCustomerById(customerId);
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteCustomer(@FormParam("id") String val){
		customerDAO.delete(customerDAO.findCustomerById(Long.parseLong(val)));
		return  "Successful";
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_XML)	
	public Customer updateCustomer(JAXBElement<Customer> cus, @PathParam("id") String id){
		cus.getValue().setPersonId(Long.valueOf(id));
		customerDAO.updateCustomer(cus.getValue());
		return customerDAO.findCustomerById(Long.valueOf(id));
	}
	
	
}
