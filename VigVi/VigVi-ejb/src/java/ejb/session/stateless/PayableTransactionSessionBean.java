/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.PayableTransaction;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.PayableTransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class PayableTransactionSessionBean implements PayableTransactionSessionBeanLocal {

    @EJB(name = "CustomerSessionSessionBeanLocal")
    private CustomerSessionSessionBeanLocal customerSessionSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public Long createNewTransaction(CustomerSessionId customerSessionId, PayableTransaction newTransaction) throws ClassIDExistException , UnknownPersistenceException, CustomerSessionNotFoundException{
        try{
        CustomerSession customerSessionEntity = customerSessionSessionBeanLocal.retrieveCustomerSessionById(customerSessionId);
        newTransaction.setCustomerSession(customerSessionEntity);
        customerSessionEntity.setPayableTransaction(newTransaction);
        em.persist(newTransaction);
        em.flush();
        return newTransaction.getPayableTransactionId();
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
    public PayableTransaction retrievePayableTransactionByPayableTransactionId(Long payableTransactionId)throws PayableTransactionNotFoundException{
        PayableTransaction emp = em.find(PayableTransaction.class, payableTransactionId);

        try{
            return emp;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new PayableTransactionNotFoundException ("PayableTransaction" + payableTransactionId + "does not exist");
        }
    }
    
    
    @Override
    public List<PayableTransaction> retrieveAllPayableTransactions() {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from PayableTransaction c");
        
        return query.getResultList();

    }
    
    @Override
    public List<PayableTransaction> retrieveAllPayableTransactionsByMerchantId(Long merchantId) {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from PayableTransaction c WHERE c.customerSession.session.gymClass.merchant.merchantId = :id");
        query.setParameter("id", merchantId);
        return query.getResultList();

    }
    
    @Override
    public List<PayableTransaction> retrieveUnpaidPayableTransactionsByMerchantId(Long merchantId) {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from PayableTransaction c WHERE c.customerSession.session.gymClass.merchant.merchantId = :id AND c.platformPaidStatus = false");
        query.setParameter("id", merchantId);
        return query.getResultList();

    }
    

    
    @Override
    public List<PayableTransaction> retrieveAllPayableTransactionsByCustomerId(Long customerId) {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from PayableTransaction c WHERE c.customerSession.customer.customerId = :id");
        query.setParameter("id", customerId);
        return query.getResultList();

    }
    
    @Override
    public List<PayableTransaction> retrieveUnpaidPayableTransactionsByCustomerId(Long customerId) {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from PayableTransaction c WHERE c.customerSession.customer.customerId = :id AND c.platformPaidStatus = false");
        query.setParameter("id", customerId);
        return query.getResultList();

    }
    
    
    @Override
    public Boolean payMerchant (Long payableTransactionId, String reference) throws PayableTransactionNotFoundException{
        
        PayableTransaction emp = retrievePayableTransactionByPayableTransactionId(payableTransactionId);
        
        emp.setPlatformPaidDate(new Date());
        emp.setPlatformPaidReference(reference);
        emp.setPlatformPaidStatus(true);
        
        return true;
        
    }
    
    @Override
    public Boolean merchantReceiveTransaction(Long payableTransactionId) throws PayableTransactionNotFoundException{
        PayableTransaction emp = retrievePayableTransactionByPayableTransactionId(payableTransactionId);
        
        emp.setMerchantReceivedDate(new Date());
        emp.setMerchantReceivedStatus(true);
        
        return true;
    }
}
