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
import entity.Wallet;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
import util.exception.AmountNotSufficientForSignUpException;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerSessionAttendanceNullException;
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
    public CustomerSessionId signUpClass(Long customerId, Long sessionId) throws ClassIDExistException , AmountNotSufficientForSignUpException, UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException, WalletNotFoundException, NoAvailableSlotException{
     try{

        Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        Session sessionEntity = sessionSessionBeanLocal.retrieveSessionBySessionId(sessionId);
        
            if(sessionEntity.getAvailableSlot() > 0){ 
                
                if(customerEntity.getWallet() != null){

                    if(customerEntity.getWallet().getCurrentBalance() >= sessionEntity.getGymClass().getClassPrice() ){

                         walletSessionBeanLocal.holdVigMoney(customerId, sessionEntity.getGymClass().getClassPrice());
                         

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

                    }else{
                        
                        throw new AmountNotSufficientForSignUpException("You do not have enough money. Please top up");
                    }
                    
                }else{
                    throw new WalletNotFoundException("You have not activated your account. Please activate");
                }

            }else{
                throw new NoAvailableSlotException("Session" + sessionId + "does not have enough slot");
            }
        }catch(WalletNotFoundException ex){
            throw new WalletNotFoundException("You have not activated your account. Please activate");
        }catch(AmountNotSufficientException ex){

             throw new AmountNotSufficientForSignUpException("You do not have enough money. Please top up");
        }catch(PersistenceException ex){
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ClassIDExistException("You have already signed up for this session.");
                }
                else
                {
                    throw new UnknownPersistenceException("Sign Up Failure");
                }
            }
            else
            {
                throw new UnknownPersistenceException("Sign Up Failure");
            }
        }
    }
    
    @Override
    public CustomerSession markAttendance(CustomerSessionId customerSessionId, boolean attendance){

        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        
        emp.setCustomerAttendance(attendance);
        
//        push this under check attendance function
//        
//        if (emp.getCustomerAttendance()== true){
//            this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.COMPLETED, currencyId);         
//        }else{
//            this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.MISSED, currencyId);
//        }
        
        return emp;
    }
    
    @Override
    public CustomerSession withdrawSession(CustomerSessionId customerSessionId) throws CurrencyNotFoundException, CustomerSessionNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException{
        CustomerSession emp = em.find(CustomerSession.class, customerSessionId);
        emp.setCustomerAttendance(false);
        Long currencyId = emp.getSession().getGymClass().getMerchant().getCurrency().getCurrencyId();
        this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.WITHDRAWN, currencyId);
 
        return emp;
    }
    
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
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
    public Boolean retrieveCustomerSessionAttendanceById (CustomerSessionId customerSessionId)throws CustomerSessionNotFoundException{
        CustomerSession emp = retrieveCustomerSessionById(customerSessionId);
        

        return emp.getCustomerAttendance();

    }
    
    @Override
    public List<CustomerSession> retrieveAllCustomerSessionsBySessionId (Long sessionId){
        Query query = em.createQuery("SELECT c from CustomerSession c WHERE c.customerSessionId.sessionId = :id");
        query.setParameter("id", sessionId);
        
        return query.getResultList();
    }
    
    @Override
    public List<CustomerSession> retrieveAllActiveCustomerSessionsBySessionId (Long sessionId){
        Query query = em.createQuery("SELECT c from CustomerSession c WHERE c.customerSessionId.sessionId = :id and c.customerSessionStatus = :status");
        query.setParameter("id", sessionId);
        query.setParameter("status", CustomerSession.CustomerSessionStatus.ACTIVE);
        
        return query.getResultList();
    }
    
    @Override
    public List<CustomerSession> retrieveAllCustomerSessions (){
        Query query = em.createQuery("SELECT c from CustomerSession c");
        
        return query.getResultList();
    }
    
    
     @Override
    public List<CustomerSession> retrieveAllCustomerSessionsByCustomerId (Long customerId){
        Query query = em.createQuery("SELECT c from CustomerSession c WHERE c.customerSessionId.customerId = :id");
        query.setParameter("id", customerId);
        
        return query.getResultList();
    }
    
    @Override
    public Boolean updateCustomerSessionStatusByCheckingAttendance(CustomerSessionId customerSessionId, Long currencyId) throws CustomerSessionAttendanceNullException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException{
        
            Boolean initialAttendance = retrieveCustomerSessionAttendanceById(customerSessionId); 
            
            System.out.println("attendance for " + customerSessionId + initialAttendance);
            
            if(initialAttendance == null){
                this.markAttendance(customerSessionId, false);
                System.out.println("updated attendance for " + customerSessionId + " from " + initialAttendance + " to false" );
            }
            
            Boolean attendance = retrieveCustomerSessionAttendanceById(customerSessionId); 
            CustomerSession emp = retrieveCustomerSessionById(customerSessionId); 
            System.out.println("attendance for " + customerSessionId + attendance);
        
            if(emp.getCustomerSessionStatus() == CustomerSessionStatus.ACTIVE ){
                if (attendance == true){
                    this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.COMPLETED, currencyId);         
                }else{
                    this.updateCustomerSessionStatus(customerSessionId, CustomerSessionStatus.MISSED, currencyId);
                }
            } 
        
        return true;
    }

    
    
    @Override
    public CustomerSession updateCustomerSessionStatus(CustomerSessionId customerSessionId, CustomerSessionStatus newStatus, Long currencyId) throws CurrencyNotFoundException, CustomerSessionNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException{
        CustomerSession emp = retrieveCustomerSessionById(customerSessionId);
        System.out.println( "************updateCustomerSessionStatus: " + customerSessionId + newStatus + currencyId);
        if (emp != null & emp.getCustomerSessionStatus()!= newStatus) {
            emp.setCustomerSessionStatus(newStatus);
            System.out.println( "************setNewCustomerSessionStatus" + newStatus);
            if (emp.getCustomerSessionStatus() == CustomerSessionStatus.COMPLETED){
             //create transaction
             double customerAmountBeforeConversion;
             customerAmountBeforeConversion = emp.getSession().getGymClass().getClassPrice();
                //deduct hold amount
                System.out.println( "************getClassPrice" + customerAmountBeforeConversion);
                Wallet wallet = walletSessionBeanLocal.deductVigMoney(emp.getCustomer().getCustomerId(), customerAmountBeforeConversion);
                System.out.println( "************updateWallet" + wallet);
             double conversionRate;
             conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
                System.out.println( "************getConversionRate" + conversionRate);
                double customerAmount = customerAmountBeforeConversion*conversionRate;
             double commissionRate;
             commissionRate = emp.getSession().getGymClass().getMerchant().getCommissionRate();
             System.out.println( "************getCommissionRate" + commissionRate);
                double platformAmount = customerAmount*commissionRate;
                double merchantAmount = customerAmount - platformAmount;
                Long payableId = payableTransactionSessionBeanLocal.createNewTransaction(customerSessionId, new PayableTransaction(customerAmount, false, merchantAmount, platformAmount));
                System.out.println( "************payable created: " + payableId);

            }
            if (emp.getCustomerSessionStatus() == CustomerSessionStatus.MISSED){
             //create transaction
             double customerAmountBeforeConversion;
             //charge 50% if they miss the class
             customerAmountBeforeConversion = emp.getSession().getGymClass().getClassPrice()*0.5;
             //deduct hold amount
             walletSessionBeanLocal.deductVigMoney(emp.getCustomer().getCustomerId(), customerAmountBeforeConversion);
             double conversionRate;
             conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
                
                double customerAmount = customerAmountBeforeConversion*conversionRate;
             double commissionRate;
             commissionRate = emp.getSession().getGymClass().getMerchant().getCommissionRate();
                double platformAmount = customerAmount*commissionRate;
                double merchantAmount = customerAmount - platformAmount;
                Long payableId = payableTransactionSessionBeanLocal.createNewTransaction(customerSessionId, new PayableTransaction(customerAmount, false, merchantAmount, platformAmount));
                System.out.println( "************payable created: " + payableId);
            }
            if (emp.getCustomerSessionStatus() == CustomerSessionStatus.CANCELLEDBYMERCHANT){
             //create transaction
             double classPrice;
             classPrice = emp.getSession().getGymClass().getClassPrice();
             //refund hold amount
             walletSessionBeanLocal.returnHoldMoney(emp.getCustomer().getCustomerId(), classPrice);
             
             System.out.println( "************amount refunded: " + classPrice);
            }
             if (emp.getCustomerSessionStatus() == CustomerSessionStatus.WITHDRAWN){
             //create transaction
             double classPrice;
             classPrice = emp.getSession().getGymClass().getClassPrice();
             //deduct money
            long long_diff = getDateDiff(emp.getSession().getSessionDate(),new Date(),TimeUnit.DAYS);
            int diff = (int) long_diff;
            System.out.println("date diff" + diff);

            if(diff < 3){
                //deduct money
                //create transaction
             double customerAmountBeforeConversion;
             //charge 20% if they withdrawn the class in less than 2 days
             customerAmountBeforeConversion = emp.getSession().getGymClass().getClassPrice()*0.2;
             //deduct hold amount
             walletSessionBeanLocal.deductVigMoney(emp.getCustomer().getCustomerId(), customerAmountBeforeConversion);
             double conversionRate;
             conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
                
                double customerAmount = customerAmountBeforeConversion*conversionRate;
             double commissionRate;
             commissionRate = emp.getSession().getGymClass().getMerchant().getCommissionRate();
                double platformAmount = customerAmount*commissionRate;
                double merchantAmount = customerAmount - platformAmount;
                Long payableId = payableTransactionSessionBeanLocal.createNewTransaction(customerSessionId, new PayableTransaction(customerAmount, false, merchantAmount, platformAmount));
                System.out.println( "************payable created: " + payableId);
            }else{
             //refund hold amount
             walletSessionBeanLocal.returnHoldMoney(emp.getCustomer().getCustomerId(), classPrice);
             System.out.println( "************amount refunded: " + classPrice);
            }
             
             
            }
            
            

        }
        return emp;
    }
    
    @Override
    public List<CustomerSession> retrieveCustomerSessionByCustomerAndSessionId(Long customerId, Long sessionId){
        Query query = em.createQuery("SELECT c from CustomerSession c WHERE c.customerSessionId.sessionId = :id and c.customerSessionId.customerId = :id2");
        query.setParameter("id", sessionId);
        query.setParameter("id2", customerId);
        
        return query.getResultList();
    }
    
    @Override
    public List<Session> retrieveCustomerSessionByCustomerId(Long customerId){
        Query query = em.createQuery("SELECT c.session from CustomerSession c WHERE c.customerSessionId.customerId = :id2 and c.customerSessionStatus = :status");
        query.setParameter("status", CustomerSession.CustomerSessionStatus.ACTIVE);
        query.setParameter("id2", customerId);
        
        return query.getResultList();
    }
}
