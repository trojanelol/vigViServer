/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Currency;
import java.util.List;
import javax.ejb.Local;
import util.exception.CurrencyNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CurrencySessionBeanLocal {

    public Long createNewCurrency(Currency newCurrency);

    public Currency updateConversionRate(long currencyId, double newConversionRate);

    public Currency retrieveCurrencyByCurrencyId(Long currencyId) throws CurrencyNotFoundException;

    public List<Currency> retrieveAllCurrencies();
    
}
