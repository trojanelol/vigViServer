/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface MerchantSessionBeanLocal {
    
    public List<Merchant> retrieveAllMerchants();

    public Merchant retrieveMerchantByMerchantId(Long merchantId) throws MerchantNotFoundException;

    public Long approveMerchant(Long merchantId);

    public Long deactivateMerchant(Long merchantId);

    public Merchant retrieveMerchantByEmail(String email) throws MerchantNotFoundException;

    public Merchant merchantLogin(String username, String password) throws InvalidLoginCredentialException;

    public Long createNewMerchant(Merchant newMerchant) throws InputDataValidationException, MerchantUsernameExistException, UnknownPersistenceException;

}
