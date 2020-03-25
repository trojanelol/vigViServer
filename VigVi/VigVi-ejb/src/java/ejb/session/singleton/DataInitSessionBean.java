/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import entity.GymClass;
import entity.Merchant;
import entity.Session;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import util.exception.GymClassNotFoundException;
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

    @EJB(name = "SessionSessionBeanLocal")
    private SessionSessionBeanLocal sessionSessionBeanLocal;

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
        } catch (ParseException | GymClassNotFoundException | ClassIDExistException | UnknownPersistenceException | MerchantNotFoundException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void initializeData() throws ClassIDExistException, UnknownPersistenceException, MerchantNotFoundException, ParseException, GymClassNotFoundException{
        if(em.find(Merchant.class, 1l)==null){
               SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
               Long merchantId = merchantSessionBeanLocal.createNewMerchant(new Merchant("Vig Gym", "Award-winning Gym (Mr.Muscle 2019)", 0.03 , "viggym@gmail.com", "password", true , "DBS" , "123-4567-890","","+65-88990099","Vig Avenue #01-12 S12345"));
               Long classId1 = classSessionBeanLocal.createNewClass(merchantId, new GymClass("Lunch Vig Gym", "Best way to spend your lunch time", "", 30.0 , 20 , "1100", "1200"));
               sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("25/03/2020")),Session.SessionStatus.ONGOING));
               sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("01/04/2020")),Session.SessionStatus.ONGOING));
               sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("08/04/2020")),Session.SessionStatus.ONGOING));
               Long classId2 = classSessionBeanLocal.createNewClass(merchantId, new GymClass("Muay Thai", "Challenge Yourself", "", 30.0 , 20 , "2000", "2130"));
               sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("25/03/2020")),Session.SessionStatus.ONGOING));
               sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("01/04/2020")),Session.SessionStatus.ONGOING));
               sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("08/04/2020")),Session.SessionStatus.ONGOING));
        }
    }
}
