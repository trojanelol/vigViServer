/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Wallet;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface WalletSessionBeanLocal {

    public Wallet retrieveWalletByCustomerId(Long customerId) throws WalletNotFoundException;

    public Wallet topUpMoney(Long customerId, double topUpAmount, Long currencyId) throws WalletNotFoundException, ClassIDExistException, UnknownPersistenceException, CurrencyNotFoundException;

    public Wallet holdVigMoney(Long customerId, double deductAmount) throws WalletNotFoundException, AmountNotSufficientException;

    public Wallet deductVigMoney(Long customerId, double deductAmount) throws WalletNotFoundException, AmountNotSufficientException;

    public Wallet returnHoldMoney(Long customerId, double returnAmount) throws WalletNotFoundException, AmountNotSufficientException;

    public List<Wallet> retrieveWalletByReferralCode(String referralCode);

    public Long activateWallet(Long customerId, Wallet newWallet, Long currencyId, String referralCode) throws ClassIDExistException, UnknownPersistenceException, CustomerNotFoundException, CurrencyNotFoundException, WalletNotFoundException, ReferralCodeNotFoundException;

    public Wallet retrieveWalletByWalletId(Long walletId) throws WalletNotFoundException;
    
}
