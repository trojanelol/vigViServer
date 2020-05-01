/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminSessionBeanLocal;
import ejb.session.stateless.ClassSessionBeanLocal;
import ejb.session.stateless.CurrencySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.CustomerSessionSessionBeanLocal;
import ejb.session.stateless.MerchantSessionBeanLocal;
import ejb.session.stateless.SessionSessionBeanLocal;
import ejb.session.stateless.WalletSessionBeanLocal;
import entity.Admin;
import entity.Currency;
import entity.Customer;
import entity.CustomerSession;
import entity.CustomerSessionId;
import entity.GymClass;
import entity.Merchant;
import entity.Session;
import entity.Wallet;
import static java.sql.Time.valueOf;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.AdminUsernameExistException;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerSessionAttendanceNullException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.GymClassNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.MerchantNotFoundException;
import util.exception.MerchantUsernameExistException;
import util.exception.NoAvailableSlotException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

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
    
    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;
    
    @PostConstruct
    public void postConstruct() {

        try {
                initializeData();        
        } catch (ParseException | CustomerNotFoundException | GymClassNotFoundException | ClassIDExistException | UnknownPersistenceException | MerchantNotFoundException | CustomerSessionNotFoundException | SessionNotFoundException | CurrencyNotFoundException | InputDataValidationException | CustomerUsernameExistException | MerchantUsernameExistException | WalletNotFoundException | AmountNotSufficientException | NoAvailableSlotException | CustomerSessionAttendanceNullException | AdminUsernameExistException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void initializeData() throws ClassIDExistException, UnknownPersistenceException, MerchantNotFoundException, ParseException, GymClassNotFoundException, CustomerNotFoundException, SessionNotFoundException, CurrencyNotFoundException, CustomerSessionNotFoundException, InputDataValidationException, CustomerUsernameExistException, MerchantUsernameExistException, WalletNotFoundException, AmountNotSufficientException, NoAvailableSlotException, CustomerSessionAttendanceNullException, AdminUsernameExistException{
        if(em.find(Admin.class, 1l)==null){
               Long adminId = adminSessionBeanLocal.createNewAdmin(new Admin("Veronica","Wong","admin","password"));
               Long singaporeRateId = currencySessionBeanLocal.createNewCurrency(new Currency(2.5,"Singapore")); 
               SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
               Long merchantId = merchantSessionBeanLocal.createNewMerchant(singaporeRateId, new Merchant("Vig Gym", "Award-winning Gym (Mr.Muscle 2019)" , "viggym@gmail.com", "password" , "DBS" , "123-4567-890","","+65-88990099","Vig Avenue #01-12 S12345"));
               Long classId1 = classSessionBeanLocal.createNewClass(merchantId, new GymClass("Lunch Vig Gym", "Best way to spend your lunch time", "", 15.0 , 20 , valueOf("10:00:00"), valueOf("12:00:00"), "Bring Towel", "Mr Muscle"));
               Long sessionId1 = sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("01/05/2020"))));
               Long sessionId2 = sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("01/04/2020"))));
               Long sessionId3 = sessionSessionBeanLocal.createNewSession(classId1, new Session((formatter.parse("08/04/2020"))));
               Long classId2 = classSessionBeanLocal.createNewClass(merchantId, new GymClass("Muay Thai", "Challenge Yourself", "", 10.0 , 20 , valueOf("20:00:00"), valueOf("21:30:00"), "Bring Towel", "Miss Veronica"));
               Long sessionId4 = sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("25/03/2020"))));
               Long sessionId5 = sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("01/04/2020"))));
               Long sessionId6 = sessionSessionBeanLocal.createNewSession(classId2, new Session((formatter.parse("08/04/2020"))));
               Long customerId1 = customerSessionBeanLocal.createNewCustomer(new Customer("customer1@gmail.com", "password", "", (formatter.parse("08/04/1998")), "Valerie", Customer.Gender.Female , "+65-89765677"));
               Long customerId2 = customerSessionBeanLocal.createNewCustomer(new Customer("customer2@gmail.com", "password", "", (formatter.parse("08/04/1998")), "John Wick", Customer.Gender.Male , "+65-89765678"));
               walletSessionBeanLocal.activateWallet(customerId1, new Wallet(100.0, "Valerie Vintage", "Pasir Panjang 12345", Wallet.CardType.Visa, "4182567812345678"),singaporeRateId);
               walletSessionBeanLocal.activateWallet(customerId2, new Wallet(100.0, "John Wick", "Pasir Pendek 12345", Wallet.CardType.Visa, "4182567812341122"),singaporeRateId);
               CustomerSessionId customerSessionId1 = customerSessionSessionBeanLocal.signUpClass(customerId1, sessionId1);
               CustomerSessionId customerSessionId2 = customerSessionSessionBeanLocal.signUpClass(customerId1, sessionId2);
               CustomerSessionId customerSessionId3 = customerSessionSessionBeanLocal.signUpClass(customerId2, sessionId1);
               CustomerSessionId customerSessionId4 = customerSessionSessionBeanLocal.signUpClass(customerId2, sessionId2);
               currencySessionBeanLocal.updateConversionRate(singaporeRateId, 3.0);
               walletSessionBeanLocal.topUpMoney(customerId2, 100, singaporeRateId);
               customerSessionSessionBeanLocal.withdrawSession(customerSessionId3);
               customerSessionSessionBeanLocal.markAttendance(customerSessionId1, true);
               customerSessionSessionBeanLocal.markAttendance(customerSessionId2, false);
               sessionSessionBeanLocal.retrieveSessionBySessionId(sessionId1);
               
               sessionSessionBeanLocal.cancelSession(sessionId1);
               sessionSessionBeanLocal.endSession(sessionId2);
               sessionSessionBeanLocal.endSession(sessionId3);
        }
    }
}
