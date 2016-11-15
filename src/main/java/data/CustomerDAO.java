package data;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import exceptions.EmailException;
import helpers.Validator;
import entities.Customer;

public class CustomerDAO {
	 public CustomerDAO() {
	    }

	    //Reusable Session Factory Object
	    private static final SessionFactory sessionFactory = buildSessionFactory();

	    private Validator validator = new Validator();

	    public void setValidation(Validator validator) {
	        this.validator = validator;
	    }

	    private static SessionFactory buildSessionFactory() {
	        //Create a standard registry Object!!
	        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
	    }
	    public Long save(Customer customer){

	        //Open a session
	        Session session = sessionFactory.openSession();
	        //Begin Transaction
	        session.beginTransaction();
	        //Use the session to save the customer
	        Long personId = Long.parseLong(String.valueOf(session.save(customer)));
	        //Commit the transaction
	        session.getTransaction().commit();
	        //Close the session
	        session.close();
	        return personId;

	    }
	    @SuppressWarnings("unchecked")
	    public List<Customer> fetchAllCustomers(){
	        //Open a session
	        Session session = sessionFactory.openSession();
	        // Create a criteria object
	        Criteria criteria = session.createCriteria(Customer.class);
	        //Get a list of all Customers according to the Criteria Object
	        List<Customer> customer = criteria.list();
	        //Close the session
	        session.close();

	        return customer;
	    }

	    public Customer findCustomerById(long id){
	        //Open a session
	        Session session = sessionFactory.openSession();
	        // Retrieve the persistent object (or null if not found)
	        Customer customer = session.get(Customer.class, id);
	        //Close the session
	        session.close();
	        //Return the object
	        return customer;

	    }

	    public void updateCustomer(Customer customer){
	        //Open a session
	        Session session = sessionFactory.openSession();
	        session.beginTransaction();
	        //Use the session to save the customer
	        session.update(customer);
	        //Commit the transaction
	        session.getTransaction().commit();
	        //Close the session
	        session.close();
	    }

	    public void delete(Customer customer){
	        //Open a session
	        Session session = sessionFactory.openSession();
	        session.beginTransaction();

	        session.delete(customer);
	        //Commit the transaction
	        session.getTransaction().commit();
	        session.close();
	    }

	    public Customer findCustomerByEmail(String email){
	        Session session = sessionFactory.openSession();
	        Object object = session.getNamedQuery("findCustomerByEmail")
	                .setParameter("email", email)
	                .uniqueResult();
	        return (Customer) object;
	    }

	    public Long save(Customer customer, Long customerId) throws EmailException {
	        customer.setPersonId(customerId);

	        validator.email(customer.getEmail());
	        //Open a session
	        Session session = sessionFactory.openSession();
	        //Begin Transaction
	        session.beginTransaction();
	        //Use the session to save the customer
	        customerId = Long.parseLong(String.valueOf(session.save(customer)));
	        //Commit the transaction
	        session.getTransaction().commit();
	        //Close the session
	        session.close();
	        return customerId;

	    }
}
