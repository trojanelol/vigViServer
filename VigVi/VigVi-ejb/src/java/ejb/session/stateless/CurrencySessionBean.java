/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Currency;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CurrencyNotFoundException;
import util.exception.SessionNotFoundException;

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
    
     @Override
    public Currency retrieveCurrencyByCurrencyId(Long currencyId)throws CurrencyNotFoundException{
        Currency currencyEntity = em.find(Currency.class, currencyId);

        try{
            return currencyEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new CurrencyNotFoundException ("Currency" + currencyId + "does not exist");
        }
    }
    
    
    @Override
     public List<Currency> retrieveAllCurrencies(){
        Query query = em.createQuery("SELECT c from Currency c");

        return query.getResultList();

    }
    
}
