/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.ReceivableTransaction;
import entity.Wallet;
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
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.ReferralCodeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class WalletSessionBean implements WalletSessionBeanLocal {

    @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    @EJB(name = "ReceivableTransactionSessionBeanLocal")
    private ReceivableTransactionSessionBeanLocal receivableTransactionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public Long activateWallet(Long customerId, Wallet newWallet, Long currencyId, String referralCode) throws ClassIDExistException , UnknownPersistenceException, CustomerNotFoundException, CurrencyNotFoundException, WalletNotFoundException, ReferralCodeNotFoundException{
        try{
        Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        newWallet.setCustomer(customerEntity);
        customerEntity.setWallet(newWallet);
        
        em.persist(newWallet);
        em.flush();
        
        System.out.println("******************* create new Wallet" + newWallet.getWalletId());
                System.out.println("******************* generated referralCode" + newWallet.getReferralCode());
        double realAmount = newWallet.getCurrentBalance();
        System.out.println("******************* referralCode" + referralCode);

        if(referralCode != ""){
            try{
                Wallet walletEmp = this.retrieveWalletByReferralCode(referralCode).get(0);
                System.out.println("******************* referred by this wallet" + walletEmp.getWalletId());
                walletEmp.setReferredUser(walletEmp.getReferredUser() + 1);
                realAmount = realAmount + 10;
                this.topUpMoney(walletEmp.getCustomer().getCustomerId(), 10, currencyId);
            }catch (NoResultException | NonUniqueResultException ex){
                throw new ReferralCodeNotFoundException(ex.getMessage());
            }
        }
        if(realAmount != 0){
            System.out.println("******************* top up real amount" + realAmount);
            double conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
            double issuedAmount = realAmount/conversionRate;
            newWallet.setCurrentBalance(issuedAmount);
            newWallet.setHoldBalance(0.0);
            receivableTransactionSessionBeanLocal.createNewTransaction(customerId, new ReceivableTransaction(realAmount, issuedAmount));
        }
        return newWallet.getWalletId();
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
    public List<Wallet> retrieveWalletByReferralCode( String referralCode){
 
        Query query = em.createQuery("SELECT c from Wallet c WHERE c.referralCode = :code");
        query.setParameter("code", referralCode);
        
        return query.getResultList();

    }
    
    @Override
    public Wallet topUpMoney(Long customerId, double topUpAmount, Long currencyId) throws WalletNotFoundException, ClassIDExistException, UnknownPersistenceException, CurrencyNotFoundException{
        Wallet walletEntity = retrieveWalletByCustomerId(customerId);
        double currentBalance = walletEntity.getCurrentBalance();
        double conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
        // convert amount to regional V dollar
        double issuedAmount = topUpAmount / conversionRate;
        walletEntity.setCurrentBalance(currentBalance + issuedAmount);
        
        receivableTransactionSessionBeanLocal.createNewTransaction(customerId, new ReceivableTransaction(topUpAmount, issuedAmount));
        
        return walletEntity;

    }
    
    //for internal use only
    @Override
    public Wallet holdVigMoney(Long customerId, double deductAmount) throws WalletNotFoundException, AmountNotSufficientException{
        Wallet walletEntity = retrieveWalletByCustomerId(customerId);

        double currentBalance = walletEntity.getCurrentBalance();

        double holdBalance = walletEntity.getHoldBalance();

        if(currentBalance >= deductAmount){
            walletEntity.setCurrentBalance(currentBalance - deductAmount);
            walletEntity.setHoldBalance(holdBalance + deductAmount);
            return walletEntity;
        }else{
            throw new AmountNotSufficientException ("Wallet from customer" + customerId + "does not have enough current Vig$");
        }
    }
    
    
    @Override
    public Wallet deductVigMoney(Long customerId, double deductAmount) throws WalletNotFoundException, AmountNotSufficientException{
        Wallet walletEntity = retrieveWalletByCustomerId(customerId);
        double holdBalance = walletEntity.getHoldBalance();
        if(holdBalance >= deductAmount){
            walletEntity.setHoldBalance(holdBalance - deductAmount);
            return walletEntity;
        }else{
            throw new AmountNotSufficientException ("Wallet from customer" + customerId + "does not have enough hold Vig$");
        }
    }
    
    @Override
    public Wallet returnHoldMoney(Long customerId, double returnAmount) throws WalletNotFoundException, AmountNotSufficientException{
        Wallet walletEntity = retrieveWalletByCustomerId(customerId);

        double holdBalance = walletEntity.getHoldBalance();
        
        double currentBalance = walletEntity.getCurrentBalance();
        
        if(holdBalance >= returnAmount){
            walletEntity.setCurrentBalance(holdBalance - returnAmount);
            walletEntity.setHoldBalance(currentBalance + returnAmount);
            return walletEntity;
        }else{
            throw new AmountNotSufficientException ("Wallet from customer" + customerId + "does not have enough hold Vig$");
        }
    }
    
    
    @Override
    public Wallet retrieveWalletByCustomerId(Long customerId)throws WalletNotFoundException{
        Customer customerEntity = em.find(Customer.class, customerId);
        Wallet walletEntity = em.find(Wallet.class, customerEntity.getWallet().getWalletId());

        try{
            return walletEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new WalletNotFoundException ("Wallet from customer" + customerId + "does not exist");
        }
    }
    
    @Override
    public Wallet retrieveWalletByWalletId(Long walletId)throws WalletNotFoundException{
       Wallet walletEntity = em.find(Wallet.class, walletId);

        try{
            return walletEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new WalletNotFoundException ("Wallet" + walletId + "does not exist");
        }
    }
}
