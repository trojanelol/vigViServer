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
import entity.PayableTransaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.NoAvailableSlotException;
import util.exception.WalletNotFoundException;
/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class CustomerSessionSessionBean implements CustomerSessionSessionBeanLocal {

    @EJB(name = "WalletSessionBeanLocal")
    private WalletSessionBeanLocal walletSessionBeanLocal;

    @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    @EJB(name = "PayableTransactionSessionBeanLocal")
    private PayableTransactionSessionBeanLocal payableTransactionSessionBeanLocal;

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public CustomerSessionId signUpClass(Long customerId, Long sessionId) throws ClassIDExistException , UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException, WalletNotFoundException,AmountNotSufficientException, NoAvailableSlotException{
     try{
        
        Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        Session sessionEntity = sessionSessionBeanLocal.retrieveSessionBySessionId(sessionId);
        
            if(sessionEntity.getAvailableSlot() > 0){ 
        
                walletSessionBeanLocal.holdVigMoney(customerId, sessionEntity.getGymClass().getClassPrice());
                CustomerSessionId customerSessionId = new CustomerSessionId(customerId, sessionId);
                CustomerSession customerSession = new CustomerSession(customerSessionId);
                customerSession.setCustomerSessionStatus(CustomerSession.CustomerSessionStatus.ACTIVE);
                customerSession.setCustomer(customerEntity);
                customerSession.setSession(sessionEntity);
                customerEntity.getSignedUpClass().add(customerSession);
                sessionEntity.getSignedUpCustomer().add(customerSession);
                sessionSessionBeanLocal.updateSessionAvailableSlot(sessionEntity.getSessionId(),sessionEntity.getAvailableSlot()-1);
                em.persist(customerSession);
                em.flush();

                return customerSessionId;

            }else{
                throw new NoAvailableSlotException("Session" + sessionId + "does not have enough slot");
            }
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
    public CustomerSession markAttendanceByCustomerSessionId(CustomerSessionId customerSessionId, boolean attendance){
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        
        emp.setCustomerAttendance(attendance);
        
        if (emp.getCustomerAttendance()== true){
            this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.COMPLETED);         
        }else{
            this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.MISSED);
        }
        
        return emp;
    }
    
    @Override
    public CustomerSession withdrawSession(CustomerSessionId customerSessionId){
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        emp.setCustomerAttendance(false);
        this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.WITHDRAWN); 
        
        return emp;
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
    public CustomerSession updateCustomerSessionStatus(CustomerSessionId customerSessionId, CustomerSessionStatus newStatus ) {
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        if (emp != null & emp.getCustomerSessionStatus()!= newStatus) {
            emp.setCustomerSessionStatus(newStatus);
        }
        return emp;
    }
    
    @Override
    public List<CustomerSession> retrieveAllCustomerSessionsBySessionId(Long sessionId){
        Query query = em.createQuery("SELECT s from CustomerSession s where s.session.sessionId = :id");
        query.setParameter("id", sessionId);
        
        return query.getResultList();
    }
    
    @Override
    public CustomerSession endClassByCustomerSessionId(CustomerSessionId customerSessionId, Long currencyId)throws CurrencyNotFoundException{
        
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        
            if (emp.getCustomerSessionStatus() == CustomerSessionStatus.COMPLETED){
             //create transaction
             double customerAmountBeforeConversion;
             customerAmountBeforeConversion = emp.getSession().getGymClass().getClassPrice();
                try {
                    //deduct hold amount
                    walletSessionBeanLocal.deductVigMoney(emp.getCustomer().getCustomerId(), customerAmountBeforeConversion);
                } catch (WalletNotFoundException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AmountNotSufficientException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
             
             
             double conversionRate;
             conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
                double customerAmount = customerAmountBeforeConversion*conversionRate;
             double commissionRate;
             commissionRate = emp.getSession().getGymClass().getMerchant().getCommissionRate();
                double platformAmount = customerAmount*commissionRate;
                double merchantAmount = customerAmount - platformAmount;
                
                try {
                    payableTransactionSessionBeanLocal.createNewTransaction(customerSessionId, new PayableTransaction(customerAmount, false, merchantAmount, platformAmount));
                } catch (ClassIDExistException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnknownPersistenceException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CustomerSessionNotFoundException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (emp.getCustomerSessionStatus() == CustomerSessionStatus.MISSED){
             //create transaction
             double customerAmountBeforeConversion;
             //charge 50% if they miss the class
             customerAmountBeforeConversion = emp.getSession().getGymClass().getClassPrice()*0.5;
                try {
                    //deduct hold amount
                    walletSessionBeanLocal.deductVigMoney(emp.getCustomer().getCustomerId(), customerAmountBeforeConversion);
                } catch (WalletNotFoundException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AmountNotSufficientException ex) {
                    Logger.getLogger(CustomerSessionSessionBean.class.getName()).log(Level.SEVERE, null, ex);
                }
             double conversionRate;
             conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
                
                double customerAmount = customerAmountBeforeConversion*conversionRate;
             double commissionRate;
             commissionRate = emp.getSession().getGymClass().getMerchant().getCommissionRate();
                double platformAmount = customerAmount*commissionRate;
                double merchantAmount = customerAmount - platformAmount;
                try {
                    payableTransactionSessionBeanLocal.createNewTransaction(customerSessionId, new PayableTransaction(customerAmount, false, merchantAmount, platformAmount));
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
           
    
    @Override
    public List<CustomerSession> updateNullAttendanceBySessionId (Long sessionId){
        
        Query query = em.createQuery("SELECT e FROM CustomerSession e WHERE e.customerAttendance IS NULL");
        List<CustomerSession> result = query.getResultList();
        for (int i = 0; i < result.size(); i++) {
                this.markAttendanceByCustomerSessionId(result.get(i).getCustomerSessionId(), false);
        }

        return query.getResultList();
    }
}
