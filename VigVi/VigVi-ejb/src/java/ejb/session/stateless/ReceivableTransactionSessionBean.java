/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReceivableTransaction;
import entity.Wallet;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.ClassIDExistException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class ReceivableTransactionSessionBean implements ReceivableTransactionSessionBeanLocal {

    @EJB(name = "WalletSessionBeanLocal")
    private WalletSessionBeanLocal walletSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public Long createNewTransaction(Long customerId, ReceivableTransaction newTransaction) throws ClassIDExistException , UnknownPersistenceException, WalletNotFoundException{
        try{
        Wallet walletEntity = walletSessionBeanLocal.retrieveWalletByCustomerId(customerId);
        newTransaction.setWallet(walletEntity);
        walletEntity.getReceivableTransactions().add(newTransaction);
        em.persist(newTransaction);
        em.flush();
        return newTransaction.getReceivableTransactionId();
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
    public List<ReceivableTransaction> retrieveAllReceivableTransactions() {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from ReceivableTransaction c");
        
        return query.getResultList();

    }
    
    @Override
    public List<ReceivableTransaction> retrieveAllReceivableTransactionsByCustomerId(Long customerId) {
            //retrievePayableTransaction
        Query query = em.createQuery("SELECT c from ReceivableTransaction c WHERE c.wallet.customer.customerId = :id");
        query.setParameter("id", customerId);
        return query.getResultList();

    }
    
    @Override
    public List retrieveIssuedAmountToDate(){
        Query query = em.createQuery("SELECT SUM(c.issuedAmount) AS totalIssuedAmount from ReceivableTransaction c");
        
        List sum = query.getResultList();
        
        return sum;
    }
}
