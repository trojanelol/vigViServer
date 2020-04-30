/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Session;
import java.util.List;
import javax.ejb.Local;
import util.exception.AmountNotSufficientException;
import util.exception.ClassIDExistException;
import util.exception.CurrencyNotFoundException;
import util.exception.CustomerSessionAttendanceNullException;
import util.exception.CustomerSessionNotFoundException;
import util.exception.GymClassNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WalletNotFoundException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface SessionSessionBeanLocal {

    public Long createNewSession(Long classId, Session newSession) throws ClassIDExistException, UnknownPersistenceException, GymClassNotFoundException;

    public Session retrieveSessionBySessionId(Long sessionId) throws SessionNotFoundException;

    public List<Session> retrieveAllOngoingSessionsByClassId(Long classId);

    public Session endSession(Long sessionId) throws SessionNotFoundException, CustomerSessionNotFoundException, CurrencyNotFoundException, WalletNotFoundException, AmountNotSufficientException, ClassIDExistException, UnknownPersistenceException, CustomerSessionAttendanceNullException;

    public List<Session> retrieveAllSessionsByClassId(Long classId);

    public List<Session> retrieveAllSessions();


    

    
}
