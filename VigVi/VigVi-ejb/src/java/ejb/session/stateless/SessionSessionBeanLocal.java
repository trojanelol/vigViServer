/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Session;
import java.util.List;
import javax.ejb.Local;
import util.exception.ClassIDExistException;
import util.exception.GymClassNotFoundException;
import util.exception.SessionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author JiaYunTeo
 */
@Local
public interface SessionSessionBeanLocal {

    public Long createNewSession(Long classId, Session newSession) throws ClassIDExistException, UnknownPersistenceException, GymClassNotFoundException;

    public Session retrieveSessionBySessionId(Long sessionId) throws SessionNotFoundException;

    public List<Session> retrieveAllOngoingSessionsByClassId(Long classId);
    
}
