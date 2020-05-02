/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReceivableTransaction;
import java.util.List;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface ReceivableTransactionSessionBeanLocal {

    public Long createNewTransaction(Long customerId, ReceivableTransaction newTransaction) throws ClassIDExistException, UnknownPersistenceException, WalletNotFoundException;

    public List<ReceivableTransaction> retrieveAllReceivableTransactions();

    public List<ReceivableTransaction> retrieveAllReceivableTransactionsByCustomerId(Long customerId);

    public List retrieveIssuedAmountToDate();

    
}
