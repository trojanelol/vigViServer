/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerSession;
import entity.CustomerSession.CustomerSessionStatus;
import entity.CustomerSessionId;
import entity.Session;
import entity.Transaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.CustomerSessionNotFoundException;
/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class CustomerSessionSessionBean implements CustomerSessionSessionBeanLocal {

    @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    @EJB(name = "TransactionSessionBeanLocal")
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public CustomerSessionId signUpClass(Long customerId, Long sessionId) throws ClassIDExistException , UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException{
        try{
        Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        Session sessionEntity = sessionSessionBeanLocal.retrieveSessionBySessionId(sessionId);
        CustomerSessionId customerSessionId = new CustomerSessionId(customerId, sessionId);
        CustomerSession customerSession = new CustomerSession(customerSessionId);
        customerSession.setCustomerSessionStatus(CustomerSession.CustomerSessionStatus.ACTIVE);
        customerSession.setCustomer(customerEntity);
        customerSession.setSession(sessionEntity);
        customerEntity.getSignedUpClass().add(customerSession);
        sessionEntity.getSignedUpCustomer().add(customerSession);
        em.persist(customerSession);
        em.flush();
        return customerSessionId;
        } catch(PersistenceException ex){
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ClassIDExistException();
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
    
    @Override
    public CustomerSession retrieveCustomerSessionById (CustomerSessionId customerSessionId)throws CustomerSessionNotFoundException{
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        
        try{
            return emp;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new CustomerSessionNotFoundException("CustomerSession" + customerSessionId + "does not exist");
        }
    }
    
    @Override
    public CustomerSession updateCustomerSessionStatus(CustomerSessionId customerSessionId, CustomerSessionStatus newStatus, Long currencyId) throws CurrencyNotFoundException{
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        if (emp != null) {
            emp.setCustomerSessionStatus(newStatus);
        }
        if (emp.getCustomerSessionStatus() == CustomerSessionStatus.COMPLETED){
         //create transaction
         double customerAmountBeforeConversion;
         customerAmountBeforeConversion = emp.getSession().getGymClass().getClassPrice();
         double conversionRate;
         conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
            double customerAmount = customerAmountBeforeConversion*conversionRate;
         double commissionRate;
         commissionRate = emp.getSession().getGymClass().getMerchant().getCommissionRate();
            double platformAmount = customerAmount*commissionRate;
            double merchantAmount = customerAmount - platformAmount;
            try {
                transactionSessionBeanLocal.createNewTransaction(customerSessionId, new Transaction(customerAmount, false, merchantAmount, platformAmount));
            } catch (ClassIDExistException ex) {
                Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CustomerSessionNotFoundException ex) {
                Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
 
        }
        return emp;
    }
}
