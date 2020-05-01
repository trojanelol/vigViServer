/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSessionId;
import entity.PayableTransaction;
import java.util.List;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.PayableTransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface PayableTransactionSessionBeanLocal {

    public Long createNewTransaction(CustomerSessionId customerSessionId, PayableTransaction newTransaction) throws ClassIDExistException, UnknownPersistenceException, CustomerSessionNotFoundException;

    public PayableTransaction retrievePayableTransactionByPayableTransactionId(Long payableTransactionId) throws PayableTransactionNotFoundException;

    public List<PayableTransaction> retrieveAllPayableTransactions();

    public Boolean merchantReceiveTransaction(Long payableTransactionId) throws PayableTransactionNotFoundException;

    public List<PayableTransaction> retrieveAllPayableTransactionsByMerchantId(Long merchantId);

    public List<PayableTransaction> retrieveAllPayableTransactionsByCustomerId(Long customerId);

    public Boolean payMerchant(Long payableTransactionId, String reference) throws PayableTransactionNotFoundException;

    public List<PayableTransaction> retrieveUnpaidPayableTransactionsByCustomerId(Long customerId);

    public List<PayableTransaction> retrieveUnpaidPayableTransactionsByMerchantId(Long merchantId);

}
