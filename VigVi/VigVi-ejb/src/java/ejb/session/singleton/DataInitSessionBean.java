/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import entity.GymClass;
import entity.Merchant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ClassIDExistException;
import util.exception.MerchantNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "ClassSessionBeanLocal")
    private ClassSessionBeanLocal classSessionBeanLocal;

    
    @PersistenceContext(unitName="VigVi-ejbPU")
    private EntityManager em;
    
    @EJB(name="MerchantSessionBeanLocal")
    private MerchantSessionBeanLocal merchantSessionBeanLocal; 
    
    @PostConstruct
    public void postConstruct() {

        try {
            initializeData();
        } catch (ClassIDExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MerchantNotFoundException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    
    private void initializeData() throws ClassIDExistException, UnknownPersistenceException, MerchantNotFoundException{
        if(em.find(Merchant.class, 1l)==null){
               merchantSessionBeanLocal.createNewMerchant(new Merchant("Vig Gym", "Award-winning Gym (Mr.Muscle 2019)", 0.03 , "viggym@gmail.com", "password", true , "DBS" , "123-4567-890","","+65-88990099","Vig Avenue #01-12 S12345"));
               int x = 1; Long y = (long)x;
               classSessionBeanLocal.createNewClass(y, new GymClass("Lunch Vig Gym", "Best way to spend your lunch time", "", 30.0 , 20 , "1100", "1200"));
        }
    }
}
