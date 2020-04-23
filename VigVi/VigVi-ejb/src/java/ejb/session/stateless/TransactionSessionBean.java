/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.Transaction;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @EJB(name = "CustomerSessionSessionBeanLocal")
    private CustomerSessionSessionBeanLocal customerSessionSessionBeanLocal;

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    
    @Override
    public Long createNewTransaction(CustomerSessionId customerSessionId, Transaction newTransaction) throws ClassIDExistException , UnknownPersistenceException, CustomerSessionNotFoundException{
        try{
        CustomerSession customerSessionEntity = customerSessionSessionBeanLocal.retrieveCustomerSessionById(customerSessionId);
        newTransaction.setCustomerSession(customerSessionEntity);
        customerSessionEntity.setTransaction(newTransaction);
        em.persist(newTransaction);
        em.flush();
        return newTransaction.getTransactionId();
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
}
