/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSessionId;
import entity.PayableTransaction;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.ClassSessionIDExistException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface PayableTransactionSessionBeanLocal {

    public Long createNewTransaction(CustomerSessionId customerSessionId, PayableTransaction newTransaction) throws ClassIDExistException, UnknownPersistenceException, CustomerSessionNotFoundException;

}
