/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.CustomerSessionSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import ejb.session.stateless.WalletSessionBeanLocal;
import entity.Currency;
import entity.Customer;
import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.GymClass;
import entity.Merchant;
import entity.Session;
import entity.Wallet;
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
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.GymClassNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "CurrencySessionBeanLocal")
    private CurrencySessionBeanLocal currencySessionBeanLocal;

    @EJB(name = "CustomerSessionSessionBeanLocal")
    private CustomerSessionSessionBeanLocal customerSessionSessionBeanLocal;


    @EJB(name = "WalletSessionBeanLocal")
    private WalletSessionBeanLocal walletSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

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
        } catch (ParseException | CustomerNotFoundException | GymClassNotFoundException | ClassIDExistException | UnknownPersistenceException | MerchantNotFoundException | CustomerSessionNotFoundException | SessionNotFoundException | CurrencyNotFoundException | InputDataValidationException | CustomerUsernameExistException | MerchantUsernameExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void initializeData() throws ClassIDExistException, UnknownPersistenceException, MerchantNotFoundException, ParseException, GymClassNotFoundException, CustomerNotFoundException, SessionNotFoundException, CurrencyNotFoundException, CustomerSessionNotFoundException, InputDataValidationException, CustomerUsernameExistException, MerchantUsernameExistException{
        if(em.find(Merchant.class, 1l)==null){
               SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
               Long merchantId = merchantSessionBeanLocal.createNewMerchant(new Merchant("Vig Gym", "Award-winning Gym (Mr.Muscle 2019)", 0.3 , "viggym@gmail.com", "password", true , "DBS" , "123-4567-890","","+65-88990099","Vig Avenue #01-12 S12345"));
               Long classId1 = classSessionBeanLocal.createNewClass(merchantId, new GymClass("Lunch Vig Gym", "Best way to spend your lunch time", "", 30.0 , 20 , "1100", "1200", "Bring Towel", "Mr Muscle"));
               Long sessionId1 = sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("25/03/2020")),Session.SessionStatus.ONGOING));
               Long sessionId2 = sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("01/04/2020")),Session.SessionStatus.ONGOING));
               Long sessionId3 = sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("08/04/2020")),Session.SessionStatus.ONGOING));
               Long classId2 = classSessionBeanLocal.createNewClass(merchantId, new GymClass("Muay Thai", "Challenge Yourself", "", 30.0 , 20 , "2000", "2130", "Bring Towel", "Miss Veronica"));
               Long sessionId4 = sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("25/03/2020")),Session.SessionStatus.ONGOING));
               Long sessionId5 = sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("01/04/2020")),Session.SessionStatus.ONGOING));
               Long sessionId6 = sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("08/04/2020")),Session.SessionStatus.ONGOING));
               Long customerId1 = customerSessionBeanLocal.createNewCustomer(new Customer("customer1@gmail.com", "password", "", (formatter.parse("08/04/1998")), "Valerie", Customer.Gender.Female , "+65-89765677"));
               Long customerId2 = customerSessionBeanLocal.createNewCustomer(new Customer("customer2@gmail.com", "password", "", (formatter.parse("08/04/1998")), "John Wick", Customer.Gender.Male , "+65-89765678"));
               walletSessionBeanLocal.createNewWallet(customerId1, new Wallet(100.0, "Valerie Vintage", "Pasir Panjang 12345", Wallet.CardType.Visa, "4182567812345678"));
               walletSessionBeanLocal.createNewWallet(customerId2, new Wallet(100.0, "John Wick", "Pasir Pendek 12345", Wallet.CardType.Visa, "4182567812341122"));
               CustomerSessionId customerSessionId1 = customerSessionSessionBeanLocal.signUpClass(customerId1, sessionId1);
               CustomerSessionId customerSessionId2 = customerSessionSessionBeanLocal.signUpClass(customerId1, sessionId2);
               CustomerSessionId customerSessionId3 = customerSessionSessionBeanLocal.signUpClass(customerId2, sessionId1);
               Long singaporeRateId = currencySessionBeanLocal.createNewCurrency(new Currency(2.5,"Singapore"));
               currencySessionBeanLocal.updateConversionRate(singaporeRateId, 3.0);
               customerSessionSessionBeanLocal.withdrawSession(customerSessionId3, singaporeRateId);
               customerSessionSessionBeanLocal.markAttendance(customerSessionId1, true, singaporeRateId);
               customerSessionSessionBeanLocal.markAttendance(customerSessionId2, false, singaporeRateId);
        }
    }
}
