/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

     @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
     
    @Override
    public Long createNewCustomer(Customer newCustomer){
        em.persist(newCustomer);
        em.flush();
        newCustomer.setCustomerStatus(Boolean.TRUE);
        return newCustomer.getCustomerId();
        
    }
    
    /**
     *
     * @return
     */
    @Override
    public List<Customer> retrieveAllCustomers() {

        Query query = em.createQuery("SELECT c from Customer c");

        return query.getResultList();

    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId)throws CustomerNotFoundException{
        Customer customerEntity = em.find(Customer.class, customerId);

        try{
            return customerEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new CustomerNotFoundException ("Customer" + customerId + "does not exist");
        }
    }
}
