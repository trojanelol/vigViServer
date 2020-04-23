/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Currency;
import javax.ejb.Local;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CurrencySessionBeanLocal {

    public Long createNewCurrency(Currency newCurrency);

    public Currency updateConversionRate(long currencyId, double newConversionRate);
    
}
