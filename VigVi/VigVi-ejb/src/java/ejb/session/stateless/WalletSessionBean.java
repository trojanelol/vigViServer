/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.ReceivableTransaction;
import entity.Wallet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
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
    public Long activateWallet(Long customerId, Wallet newWallet, Long currencyId) throws ClassIDExistException , UnknownPersistenceException, CustomerNotFoundException, CurrencyNotFoundException, WalletNotFoundException{
        try{
        Customer customerEntity = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
        newWallet.setCustomer(customerEntity);
        customerEntity.setWallet(newWallet);
        em.persist(newWallet);
        em.flush();
        double realAmount = newWallet.getCurrentBalance();
        if(realAmount != 0){
            double conversionRate = currencySessionBeanLocal.retrieveCurrencyByCurrencyId(currencyId).getConversionRate();
            double issuedAmount = realAmount/conversionRate;
            newWallet.setCurrentBalance(issuedAmount);
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
    
    public Wallet deductMoney(Long customerId, double deductAmount) throws WalletNotFoundException, AmountNotSufficientException{
        Wallet walletEntity = retrieveWalletByCustomerId(customerId);
        double currentBalance = walletEntity.getCurrentBalance();
        if(currentBalance >= deductAmount){
            walletEntity.setCurrentBalance(currentBalance - deductAmount);

            return walletEntity;
        }else{
            throw new AmountNotSufficientException ("Wallet from customer" + customerId + "does not have enough Vig$");
        }
    }
    
    
    @Override
    public Wallet retrieveWalletByCustomerId(Long customerId)throws WalletNotFoundException{
        Wallet walletEntity = em.find(Wallet.class, customerId);

        try{
            return walletEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new WalletNotFoundException ("Wallet from customer" + customerId + "does not exist");
        }
    }
}
