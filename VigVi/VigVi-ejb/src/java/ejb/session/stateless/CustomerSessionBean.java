/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
@Local(CustomerSessionBeanLocal.class)
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
     private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomerSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Customer retrieveCustomerByEmail(String email) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.customerEmail = :inCustomerEmail");
        query.setParameter("inCustomerEmail", email);
        
        try
        {
            return (Customer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer email " + email + " does not exist!");
        }
    }
    
    @Override
    public Customer customerLogin(String email, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Customer customer = retrieveCustomerByEmail(email);            
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customer.getSalt()));
            
            if(customer.getCustomerPw().equals(passwordHash))
            {             
                return customer;
            }
            else
            {
                throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
        }
    }
    
    @Override
    public Long createNewCustomer(Customer newCustomer)throws InputDataValidationException, CustomerUsernameExistException, UnknownPersistenceException{
        
        try
        {
            Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newCustomer);
                em.flush();

                newCustomer.setCustomerStatus(Boolean.TRUE);
                return newCustomer.getCustomerId();
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new CustomerUsernameExistException("Customer Email Exists.");
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } 
     
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
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
    
    @Override
    public void updateCustomer(Customer customer) throws InputDataValidationException, CustomerNotFoundException
    {
        if(customer != null && customer.getCustomerId()!= null)
        {
            Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(customer);
        
            if(constraintViolations.isEmpty())
            {
                Customer customerEntityToUpdate = this.retrieveCustomerByCustomerId(customer.getCustomerId());
                    
                    customerEntityToUpdate.setCustomerName(customer.getCustomerName());
                    customerEntityToUpdate.setCustomerBday(customer.getCustomerBday());
                    customerEntityToUpdate.setCustomerContactNo(customer.getCustomerContactNo());
                    customerEntityToUpdate.setCustomerEmail(customer.getCustomerEmail());
                    customerEntityToUpdate.setCustomerGender(customer.getCustomerGender());
                    customerEntityToUpdate.setCustomerPw(customer.getCustomerPw());
                    customerEntityToUpdate.setCustomerStatus(customer.getCustomerStatus());

            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID not provided for product to be updated");
        }
    }
    
        @Override
    public Long approveCustomer(Long customerId){
        Customer customerEntity = em.find(Customer.class, customerId);
        
        customerEntity.setCustomerStatus(Boolean.TRUE);
        
        return customerEntity.getCustomerId();
    }
    
    @Override
    public Long deactivateCustomer(Long customerId){
        Customer customerEntity = em.find(Customer.class, customerId);
        
        customerEntity.setCustomerStatus(Boolean.FALSE);
        
        return customerEntity.getCustomerId();
    }
}
