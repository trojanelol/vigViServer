/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSession;
import entity.CustomerSessionId;
import java.util.List;
import javax.ejb.Local;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerSessionAttendanceNullException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.NoAvailableSlotException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CustomerSessionSessionBeanLocal {

    
    public CustomerSession retrieveCustomerSessionById(CustomerSessionId customerSessionId) throws CustomerSessionNotFoundException;

    public CustomerSessionId signUpClass(Long customerId, Long sessionId) throws ClassIDExistException, UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException, WalletNotFoundException, AmountNotSufficientException, NoAvailableSlotException;  

    public List<CustomerSession> retrieveAllCustomerSessionsBySessionId(Long sessionId);

    public List<CustomerSession> retrieveAllCustomerSessionsByCustomerId(Long customerId);

    public CustomerSession markAttendance(CustomerSessionId customerSessionId, boolean attendance);
    
    public CustomerSession updateCustomerSessionStatus(CustomerSessionId customerSessionId, CustomerSession.CustomerSessionStatus newStatus, Long currencyId) throws CurrencyNotFoundException, CustomerSessionNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException;

    public Boolean updateCustomerSessionStatusByCheckingAttendance(CustomerSessionId customerSessionId, Long currencyId) throws CustomerSessionAttendanceNullException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException;

    public Boolean retrieveCustomerSessionAttendanceById(CustomerSessionId customerSessionId) throws CustomerSessionNotFoundException;

    public CustomerSession withdrawSession(CustomerSessionId customerSessionId) throws CurrencyNotFoundException, CustomerSessionNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException;

    public List<CustomerSession> retrieveAllCustomerSessions();

}
