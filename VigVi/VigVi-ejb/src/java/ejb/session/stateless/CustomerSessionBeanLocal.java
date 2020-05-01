/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CustomerSessionBeanLocal {



    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public List<Customer> retrieveAllCustomers();

    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public Long createNewCustomer(Customer newCustomer) throws InputDataValidationException, CustomerUsernameExistException, UnknownPersistenceException;

    public void updateCustomer(Customer customer) throws InputDataValidationException, CustomerNotFoundException;

    public Long deactivateCustomer(Long customerId);

    public Long approveCustomer(Long customerId);
    
}
