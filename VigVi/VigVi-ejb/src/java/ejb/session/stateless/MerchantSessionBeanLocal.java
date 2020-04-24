/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import java.util.List;
import javax.ejb.Local;
import util.exception.MerchantNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface MerchantSessionBeanLocal {
    
    public Long createNewMerchant(Merchant newMerchant);
    public List<Merchant> retrieveAllMerchants();

    public Merchant retrieveMerchantByMerchantId(Long merchantId) throws MerchantNotFoundException;

    public Long approveMerchant(Long merchantId);

    public Long deactivateMerchant(Long merchantId);

}
