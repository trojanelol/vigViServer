/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerSession;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.CustomerNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface CustomerSessionSessionBeanLocal {

    public Long signUpClass(Long customerId, Long sessionId) throws ClassIDExistException, UnknownPersistenceException, CustomerNotFoundException, SessionNotFoundException;
    
}
