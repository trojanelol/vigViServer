/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Currency;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JiaYunTeo
 */
@Stateless
public class CurrencySessionBean implements CurrencySessionBeanLocal {

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createNewCurrency(Currency newCurrency){
        em.persist(newCurrency);
        em.flush();
        
        return newCurrency.getCurrencyId();
        
    }
    
    @Override
    public Currency updateConversionRate(long currencyId, double newConversionRate){
        Currency emp = em.find(Currency.class, currencyId);
        if (emp != null) {
            emp.setConversionRate(newConversionRate);
        }
        return emp;
        
    }
    
}
