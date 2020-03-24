/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Merchant;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MerchantNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Stateless


public class MerchantSessionBean implements MerchantSessionBeanLocal {

    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Long createNewMerchant(Merchant newMerchant){
        em.persist(newMerchant);
        em.flush();
        
        return newMerchant.getMerchantId();
        
    }
    
    /**
     *
     * @return
     */
    @Override
    public List<Merchant> retrieveAllMerchants() {

        Query query = em.createQuery("SELECT m from Merchant m");

        return query.getResultList();

    }
    
    @Override
    public Merchant retrieveMerchantByMerchantId(Long merchantId)throws MerchantNotFoundException{
        Merchant merchantEntity = em.find(Merchant.class, merchantId);

        try{
            return merchantEntity;
        }catch (NoResultException | NonUniqueResultException ex){
            throw new MerchantNotFoundException ("Merchant" + merchantId + "does not exist");
        }
    }
    
    
    
    
}
