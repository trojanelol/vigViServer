/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Wallet;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.CustomerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface WalletSessionBeanLocal {

    public Long createNewWallet(Long customerId, Wallet newWallet) throws ClassIDExistException, UnknownPersistenceException, CustomerNotFoundException;

    public Wallet retrieveWalletByCustomerId(Long customerId) throws WalletNotFoundException;
    
}
