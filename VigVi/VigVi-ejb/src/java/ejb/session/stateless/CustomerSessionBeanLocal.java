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

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createNewCustomer(Customer newCustomer);

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public List<Customer> retrieveAllCustomers();
    
}
