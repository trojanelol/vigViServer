/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSession;
import entity.CustomerSessionId;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CustomerSessionSessionBeanLocal {

    public CustomerSessionId signUpClass(Long customerId, Long sessionId) throws ClassIDExistException, UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException;
    
    public CustomerSession updateCustomerSessionStatus(CustomerSessionId customerSessionId, CustomerSession.CustomerSessionStatus newStatus, Long currencyId) throws CurrencyNotFoundException;

    public CustomerSession retrieveCustomerSessionById(CustomerSessionId customerSessionId) throws CustomerSessionNotFoundException;

    public CustomerSession markAttendance(CustomerSessionId customerSessionId, boolean attendance, Long currencyId) throws CurrencyNotFoundException;

    public CustomerSession withdrawSession(CustomerSessionId customerSessionId, Long currencyId) throws CurrencyNotFoundException;
}
