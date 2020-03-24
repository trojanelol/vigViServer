/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.Merchant;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author JiaYunTeo
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    
    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    @EJB(name="MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal; 
    
    @PostConstruct
    void PostConstruct(){
        if(em.find(Merchant.class, 1l)==null){
               merchantSessionBeanLocal.createNewMerchant(new Merchant("Vig Gym", "Award-winning Gym (Mr.Muscle 2019)", 0.03 , "viggym@gmail.com", "password", true , "DBS" , "123-4567-890",""));
        }
    }
}
